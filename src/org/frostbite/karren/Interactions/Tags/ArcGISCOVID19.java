/*
 * Copyright (c) 2020 Owen Bennett.
 *  You may use, distribute and modify this code under the terms of the MIT licence.
 *  You should have obtained a copy of the MIT licence with this software,
 *  if not please obtain one from https://opensource.org/licences/MIT
 *
 *
 *
 */

package org.frostbite.karren.Interactions.Tags;

import org.frostbite.karren.Interactions.Interaction;
import org.frostbite.karren.Interactions.InteractionResult;
import org.frostbite.karren.Interactions.Tag;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.net.ssl.HttpsURLConnection;
import javax.net.ssl.SSLContext;
import javax.net.ssl.TrustManager;
import javax.net.ssl.X509TrustManager;
import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.security.SecureRandom;
import java.security.cert.X509Certificate;
import java.util.Date;

public class ArcGISCOVID19 extends Tag {

    final TrustManager[] trustAllCertificates = new TrustManager[]{
            new X509TrustManager() {
                @Override
                public X509Certificate[] getAcceptedIssuers() {
                    return null; // Not relevant.
                }

                @Override
                public void checkClientTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }

                @Override
                public void checkServerTrusted(X509Certificate[] certs, String authType) {
                    // Do nothing. Just allow them all.
                }
            }
    };

    @Override
    public String handleTemplate(String msg, Interaction interaction, InteractionResult result) {
        int confirmed = 0;
        int recovered = 0;
        int deaths = 0;
        long lastUpdate = 0L;
        String country = null;
        String province = null;
        boolean getData = false;
        interaction.setEmbedFooter("Using ArcGIS API");
        interaction.setEmbedURL("https://experience.arcgis.com/experience/685d0ace521648f8a5beeeee1b9125cd");
        if(interaction.hasParameter()) {
            try {
                String search = URLEncoder.encode(interaction.getParameter().trim(), StandardCharsets.UTF_8.toString());
                String resultString = getText("https://services1.arcgis.com/0MSEUqKaxRlEPj5g/arcgis/rest/services/Coronavirus_2019_nCoV_Cases/FeatureServer/1/query?where=Province_State%3D%27" + search + "%27+OR+Country_Region%3D%27" + search + "%27&objectIds=&time=&geometry=&geometryType=esriGeometryEnvelope&inSR=&spatialRel=esriSpatialRelIntersects&resultType=none&distance=0.0&units=esriSRUnit_Meter&returnGeodetic=false&outFields=*&returnGeometry=true&featureEncoding=esriDefault&multipatchOption=xyFootprint&maxAllowableOffset=&geometryPrecision=&outSR=&datumTransformation=&applyVCSProjection=false&returnIdsOnly=false&returnUniqueIdsOnly=false&returnCountOnly=false&returnExtentOnly=false&returnQueryGeometry=false&returnDistinctValues=false&cacheHint=false&orderByFields=&groupByFieldsForStatistics=&outStatistics=&having=&resultOffset=&resultRecordCount=&returnZ=false&returnM=false&returnExceededLimitFeatures=true&quantizationParameters=&sqlFormat=none&f=pjson&token=");
                JSONObject resultObj = new JSONObject(resultString);
                JSONArray dataArray = resultObj.getJSONArray("features");

                //Process dataset
                for(int i=0; i<dataArray.length(); i++){
                    //Get location data
                    JSONObject obj = dataArray.getJSONObject(i).getJSONObject("attributes");
                    confirmed += obj.getInt("Confirmed");
                    recovered += obj.getInt("Recovered");
                    deaths += obj.getInt("Deaths");
                    long updated = obj.getLong("Last_Update");
                    if(updated > lastUpdate)
                        lastUpdate = updated;
                    if(dataArray.length()==1 && obj.optString("Province_State", null)!=null){
                        province = obj.getString("Province_State");
                        msg = interaction.getRandomTemplate("province").getTemplate();
                    }
                    country = obj.getString("Country_Region");
                }

                if(dataArray.length()>0)
                    getData = true;
            } catch (Exception e) {
                e.printStackTrace();
            }

            if(getData){
                if(province!=null)
                    msg = interaction.replaceMsg(msg, "%province", province);
                msg = interaction.replaceMsg(msg, "%confirmed", Integer.toString(confirmed));
                msg = interaction.replaceMsg(msg, "%recovered", Integer.toString(recovered));
                msg = interaction.replaceMsg(msg, "%deaths", Integer.toString(deaths));
                Date lastUpdated = new Date(lastUpdate);
                msg = interaction.replaceMsg(msg, "%lastUpdate", lastUpdated.toString());
                msg = interaction.replaceMsg(msg, "%country", country);
            } else {
                msg = interaction.getRandomTemplate("fail").getTemplate();
            }
        } else {
            msg = interaction.getRandomTemplate("noparam").getTemplate();
        }

        return msg;
    }

    @Override
    public String getTagName() {
        return super.getTagName();
    }

    public String getText(String url) throws Exception {
        URL website = new URL(url);
        SSLContext sc = SSLContext.getInstance("SSL");
        sc.init(null, trustAllCertificates, new SecureRandom());
        HttpsURLConnection connection = (HttpsURLConnection) website.openConnection();
        connection.setSSLSocketFactory(sc.getSocketFactory());
        BufferedReader in = new BufferedReader(
                new InputStreamReader(
                        connection.getInputStream()));

        StringBuilder response = new StringBuilder();
        String inputLine;

        while ((inputLine = in.readLine()) != null)
            response.append(inputLine);

        in.close();

        return response.toString();
    }
}
