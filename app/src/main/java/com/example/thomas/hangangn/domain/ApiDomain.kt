package com.example.thomas.hangangn.domain

import com.google.gson.annotations.SerializedName



data class ApiDomain(
    @SerializedName("GeoInfoJokguWGS") var geoInfoJokguWGS: GeoInfoJokguWGS
)

data class GeoInfoJokguWGS(
    @SerializedName("list_total_count") var listTotalCount: Int,
    @SerializedName("RESULT") var rESULT: RESULT,
    @SerializedName("row") var row: List<Row>
)

data class Row(
    @SerializedName("OBJECTID") var oBJECTID: Double,
    @SerializedName("FTC") var fTC: String,
    @SerializedName("IDN") var iDN: String,
    @SerializedName("GIGU") var gIGU: String,
    @SerializedName("JONAME") var jONAME: String,
    @SerializedName("TEL") var tEL: String,
    @SerializedName("PIC") var pIC: String,
    @SerializedName("RMK") var rMK: String,
    @SerializedName("H_ORG_CODE") var hORGCODE: String,
    @SerializedName("MGE_NAM") var mGENAM: String,
    @SerializedName("LAT") var lAT: String,
    @SerializedName("LNG") var lNG: String
)

data class RESULT(
    @SerializedName("CODE") var cODE: String,
    @SerializedName("MESSAGE") var mESSAGE: String
)