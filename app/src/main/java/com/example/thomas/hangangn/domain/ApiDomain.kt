package com.example.thomas.hangangn.domain

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName


data class ApiDomain(
//        @Expose @SerializedName("GeoInfoJokguWGS") var geoInfoWGS: GeoInfoJokguWGS?=null
        @Expose @SerializedName(value = "GeoInfoWGS" ,alternate = arrayOf(
                "GeoInfoBadmintonWGS","GeoInfoWrestlingWGS","GeoInfoLawnBowlingWGS","GeoInfoInlineSkateWGS",
                "GeoInfoJokguWGS","GeoInfoTrackWGS","GeoInfoWoodballWGS","GeoInfoXgameWGS","GeoInfoArcheryWGS",
                "GeoInfoParkGolfWGS","GeoInfoTennisWGS","GeoInfoGateballWGS","GeoInfoBasketballWGS","GeoInfoVolleyballWGS","GeoInfoSoccerWGS",
                "GeoInfoNatureStudyWGS","GeoInfoBaseballWGS","GeoInfoPlaygroundWGS",
                "GeoInfoDrinkWaterWGS","GeoInfoStoreWGS","GeoInfoParkParkingWGS","GeoInfoBicycleLendWGS","GeoInfoBicycleStorageWGS","GeoInfoParkOfficeWGS",
                "GeoInfoWaterLeisureWGS","GeoInfoWaterTrainingWGS","GeoInfoDuckBoatWGS","GeoInfoWaterTaxiWGS","GeoInfoPoolWGS",
                "GeoInfoWorkRoadWGS","GeoInfoInlineRoadWGS")) var geoInfoWGS: GeoInfoJokguWGS?=null
)

data class GeoInfoJokguWGS(
        @Expose @SerializedName("list_total_count") var listTotalCount: Int,
        @Expose @SerializedName("RESULT") var rESULT: RESULT,
        @Expose @SerializedName("row") var row: List<Row>
)

data class Row(
        @Expose @SerializedName("GIGU") var gIGU: String,
        @Expose @SerializedName("LAT") var lAT: String,
        @Expose @SerializedName("LNG") var lNG: String
)

//data class Row(
//        @Expose var oBJECTID: Double,
//        var fTC: String,
//        var iDN: String,
//        @Expose @SerializedName("GIGU") var gIGU: String,
//        var jONAME: String,
//        var tEL: String,
//        var pIC: String,
//        var rMK: String,
//        var hORGCODE: String,
//        var mGENAM: String,
//        @Expose @SerializedName("LAT") var lAT: String,
//        @Expose @SerializedName("LNG") var lNG: String
//)

data class RESULT(
        @Expose @SerializedName("CODE") var cODE: String,
        @Expose @SerializedName("MESSAGE") var mESSAGE: String
)