package com.adamratzman.spotify.utils

/*
 * Copyright (C) 2012-2019 Neo Visionaries Inc.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */


/**
 * [ISO 3166-1](http://en.wikipedia.org/wiki/ISO_3166-1) country code.
 *
 *
 *
 * Enum names of this enum themselves are represented by
 * [ISO 3166-1 alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2)
 * code (2-letter upper-case alphabets). There are instance methods to get the
 * country name ([.getName]), the
 * [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3)
 * code ([.getAlpha3]) and the
 * [ISO 3166-1 numeric](http://en.wikipedia.org/wiki/ISO_3166-1_numeric)
 * code ([.getNumeric]).
 * In addition, there are static methods to get a `CountryCode` instance that
 * corresponds to a given alpha-2/alpha-3/numeric code ([.getByCode],
 * [.getByCode]).
 *
 *
 * <pre style="background-color: #EEEEEE; margin-left: 2em; margin-right: 2em; border: 1px solid black; padding: 0.5em;">
 * <span style="color: darkgreen;">// List all the country codes.</span>
 * for (CountryCode code : CountryCode.values())
 * {
 * <span style="color: darkgreen;">// For example, "[US] United States" is printed.</span>
 * System.out.format(<span style="color: darkred;">"[%s] %s\n"</span>, code, code.[.getName]);
 * }
 *
 * <span style="color: darkgreen;">// Get a CountryCode instance by ISO 3166-1 code.</span>
 * CountryCode code = CountryCode.[getByCode][.getByCode](<span style="color: darkred;">"JP"</span>);
 *
 * <span style="color: darkgreen;">// Print all the information. Output will be:</span>
 * <span style="color: darkgreen;">//</span>
 * <span style="color: darkgreen;">//     Country name            = Japan</span>
 * <span style="color: darkgreen;">//     ISO 3166-1 alpha-2 code = JP</span>
 * <span style="color: darkgreen;">//     ISO 3166-1 alpha-3 code = JPN</span>
 * <span style="color: darkgreen;">//     ISO 3166-1 numeric code = 392</span>
 * <span style="color: darkgreen;">//     Assignment state        = OFFICIALLY_ASSIGNED</span>
 * <span style="color: darkgreen;">//</span>
 * System.out.println(<span style="color: darkred;">"Country name            = "</span> + code.[.getName]);
 * System.out.println(<span style="color: darkred;">"ISO 3166-1 alpha-2 code = "</span> + code.[.getAlpha2]);
 * System.out.println(<span style="color: darkred;">"ISO 3166-1 alpha-3 code = "</span> + code.[.getAlpha3]);
 * System.out.println(<span style="color: darkred;">"ISO 3166-1 numeric code = "</span> + code.[.getNumeric]);
 * System.out.println(<span style="color: darkred;">"Assignment state        = "</span> + code.[.getAssignment]);
 *
 * <span style="color: darkgreen;">// Convert to a Locale instance.</span>
 * [Locale] locale = code.[.toLocale];
 *
 * <span style="color: darkgreen;">// Get a CountryCode by a Locale instance.</span>
 * code = CountryCode.[getByLocale][.getByLocale](locale);
 *
 * <span style="color: darkgreen;">// Get the currency of the country.</span>
 * [Currency] currency = code.[.getCurrency];
 *
 * <span style="color: darkgreen;">// Get a list by a regular expression for names.
 * //
 * // The list will contain:
 * //
 * //     CountryCode.AE : United Arab Emirates
 * //     CountryCode.GB : United Kingdom
 * //     CountryCode.TZ : Tanzania, United Republic of
 * //     CountryCode.UK : United Kingdom
 * //     CountryCode.UM : United States Minor Outlying Islands
 * //     CountryCode.US : United States
 * //</span>
 * List&lt;CountryCode&gt; list = CountryCode.[findByName][.findByName](<span style="color: darkred;">".*United.*"</span>);
 *
 * <span style="color: darkgreen;">
 * // For backward compatibility for older versions than 1.16, some
 * // 4-letter ISO 3166-3 codes are accepted by getByCode(String, boolean)
 * // and its variants. To be concrete:
 * //
 * //     [ANHH](https://en.wikipedia.org/wiki/ISO_3166-3#ANHH) : CountryCode.AN
 * //     [BUMM](https://en.wikipedia.org/wiki/ISO_3166-3#BUMM) : CountryCode.BU
 * //     [CSXX](https://en.wikipedia.org/wiki/ISO_3166-3#CSXX) : CountryCode.CS
 * //     [NTHH](https://en.wikipedia.org/wiki/ISO_3166-3#NTHH) : CountryCode.NT
 * //     [TPTL](https://en.wikipedia.org/wiki/ISO_3166-3#TPTL) : CountryCode.TP
 * //     [YUCS](https://en.wikipedia.org/wiki/ISO_3166-3#YUCS) : CountryCode.YU
 * //     [ZRCD](https://en.wikipedia.org/wiki/ISO_3166-3#ZRCD) : CountryCode.ZR
 * //</span>
 * code = CountryCode.[getByCode][.getByCode](<span style="color: darkred;">"ANHH"</span>);
</pre> *
 *
 * @author Takahiko Kawasaki
 */
enum class Market private constructor(
    /**
     * Get the country name.
     *
     * @return
     * The country name.
     */
    val marketName: String,
    /**
     * Get the [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) code.
     *
     * @return
     * The [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) code.
     * Some country codes reserved exceptionally (such as [.EU])
     * returns `null`.
     * [CountryCode.UNDEFINED] returns `null`, too.
     */
    val alpha3: String?,
    /**
     * Get the [ISO 3166-1 numeric](http://en.wikipedia.org/wiki/ISO_3166-1_numeric) code.
     *
     * @return
     * The [ISO 3166-1 numeric](http://en.wikipedia.org/wiki/ISO_3166-1_numeric) code.
     * Country codes reserved exceptionally (such as [.EU])
     * returns `-1`.
     * [CountryCode.UNDEFINED] returns `-1`, too.
     */
    val numeric: Int,
    /**
     * Get the assignment state of this country code in ISO 3166-1.
     *
     * @return
     * The assignment state.
     *
     * @see [Decoding table of ISO 3166-1 alpha-2 codes](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2.Decoding_table)
     */
    val assignment: Assignment
) {
    /**
     * [Ascension Island](http://en.wikipedia.org/wiki/Ascension_Island)
     * [[AC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AC), ASC, -1,
     * Exceptionally reserved]
     */
    AC("Ascension Island", "ASC", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Andorra](http://en.wikipedia.org/wiki/Andorra)
     * [[AD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AD), AND, 16,
     * Officially assigned]
     */
    AD("Andorra", "AND", 20, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United Arab Emirates](http://en.wikipedia.org/wiki/United_Arab_Emirates)
     * [[AE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AE), AE, 784,
     * Officially assigned]
     */
    AE("United Arab Emirates", "ARE", 784, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Afghanistan](http://en.wikipedia.org/wiki/Afghanistan)
     * [[AF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AF), AFG, 4,
     * Officially assigned]
     */
    AF("Afghanistan", "AFG", 4, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Antigua and Barbuda](http://en.wikipedia.org/wiki/Antigua_and_Barbuda)
     * [[AG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AG), ATG, 28,
     * Officially assigned]
     */
    AG("Antigua and Barbuda", "ATG", 28, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Anguilla](http://en.wikipedia.org/wiki/Anguilla)
     * [[AI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AI), AIA, 660,
     * Officially assigned]
     */
    AI("Anguilla", "AIA", 660, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Albania](http://en.wikipedia.org/wiki/Albania)
     * [[AL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AL), ALB, 8,
     * Officially assigned]
     */
    AL("Albania", "ALB", 8, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Armenia](http://en.wikipedia.org/wiki/Armenia)
     * [[AM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AM), ARM, 51,
     * Officially assigned]
     */
    AM("Armenia", "ARM", 51, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Netherlands Antilles](http://en.wikipedia.org/wiki/Netherlands_Antilles)
     * [[AN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AN), ANT, 530,
     * Traditionally reserved]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `ANT`
     * (not `[ANHH](http://en.wikipedia.org/wiki/ISO_3166-3#ANHH)`).
     *
     */
    AN("Netherlands Antilles", "ANT", 530, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Angola](http://en.wikipedia.org/wiki/Angola)
     * [[AO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AO), AGO, 24,
     * Officially assigned]
     */
    AO("Angola", "AGO", 24, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Antarctica](http://en.wikipedia.org/wiki/Antarctica)
     * [[AQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AQ), ATA, 10,
     * Officially assigned]
     */
    AQ("Antarctica", "ATA", 10, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Argentina](http://en.wikipedia.org/wiki/Argentina)
     * [[AR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AR), ARG, 32,
     * Officially assigned]
     */
    AR("Argentina", "ARG", 32, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [American Samoa](http://en.wikipedia.org/wiki/American_Samoa)
     * [[AS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AS), ASM, 16,
     * Officially assigned]
     */
    AS("American Samoa", "ASM", 16, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Austria](http://en.wikipedia.org/wiki/Austria)
     * [[AT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AT), AUT, 40,
     * Officially assigned]
     */
    AT("Austria", "AUT", 40, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Australia](http://en.wikipedia.org/wiki/Australia)
     * [[AU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AU), AUS, 36,
     * Officially assigned]
     */
    AU("Australia", "AUS", 36, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Aruba](http://en.wikipedia.org/wiki/Aruba)
     * [[AW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AW), ABW, 533,
     * Officially assigned]
     */
    AW("Aruba", "ABW", 533, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [land Islands](http://en.wikipedia.org/wiki/%C3%85land_Islands)
     * [[AX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AX), ALA, 248,
     * Officially assigned]
     *
     *
     *
     * The country name was changed from "\u212Bland Islands" (up to 1.14)
     * to "\u00C5land Islands" (since 1.15).
     *
     */
    AX("\u00C5land Islands", "ALA", 248, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Azerbaijan](http://en.wikipedia.org/wiki/Azerbaijan)
     * [[AZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AZ), AZE, 31,
     * Officially assigned]
     */
    AZ("Azerbaijan", "AZE", 31, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bosnia and Herzegovina](http://en.wikipedia.org/wiki/Bosnia_and_Herzegovina)
     * [[BA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BA), BIH, 70,
     * Officially assigned]
     */
    BA("Bosnia and Herzegovina", "BIH", 70, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Barbados](http://en.wikipedia.org/wiki/Barbados)
     * [[BB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BB), BRB, 52,
     * Officially assigned]
     */
    BB("Barbados", "BRB", 52, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bangladesh](http://en.wikipedia.org/wiki/Bangladesh)
     * [[BD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BD), BGD, 50,
     * Officially assigned]
     */
    BD("Bangladesh", "BGD", 50, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Belgium](http://en.wikipedia.org/wiki/Belgium)
     * [[BE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BE), BEL, 56,
     * Officially assigned]
     */
    BE("Belgium", "BEL", 56, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Burkina Faso](http://en.wikipedia.org/wiki/Burkina_Faso)
     * [[BF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BF), BFA, 854,
     * Officially assigned]
     */
    BF("Burkina Faso", "BFA", 854, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bulgaria](http://en.wikipedia.org/wiki/Bulgaria)
     * [[BG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BG), BGR, 100,
     * Officially assigned]
     */
    BG("Bulgaria", "BGR", 100, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bahrain](http://en.wikipedia.org/wiki/Bahrain)
     * [[BH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BH), BHR, 48,
     * Officially assigned]
     */
    BH("Bahrain", "BHR", 48, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Burundi](http://en.wikipedia.org/wiki/Burundi)
     * [[BI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BI), BDI, 108,
     * Officially assigned]
     */
    BI("Burundi", "BDI", 108, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Benin](http://en.wikipedia.org/wiki/Benin)
     * [[BJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BJ), BEN, 204,
     * Officially assigned]
     */
    BJ("Benin", "BEN", 204, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Barthlemy](http://en.wikipedia.org/wiki/Saint_Barth%C3%A9lemy)
     * [[BL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BL), BLM, 652,
     * Officially assigned]
     */
    BL("Saint Barth\u00E9lemy", "BLM", 652, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bermuda](http://en.wikipedia.org/wiki/Bermuda)
     * [[BM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BM), BMU, 60,
     * Officially assigned]
     */
    BM("Bermuda", "BMU", 60, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Brunei Darussalam](http://en.wikipedia.org/wiki/Brunei)
     * [[BN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BN), BRN, 96,
     * Officially assigned]
     */
    BN("Brunei Darussalam", "BRN", 96, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bolivia, Plurinational State of](http://en.wikipedia.org/wiki/Bolivia)
     * [[BO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BO), BOL, 68,
     * Officially assigned]
     */
    BO("Bolivia, Plurinational State of", "BOL", 68, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bonaire, Sint Eustatius and Saba](http://en.wikipedia.org/wiki/Caribbean_Netherlands)
     * [[BQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BQ), BES, 535,
     * Officially assigned]
     */
    BQ("Bonaire, Sint Eustatius and Saba", "BES", 535, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Brazil](http://en.wikipedia.org/wiki/Brazil)
     * [[BR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BR), BRA, 76,
     * Officially assigned]
     */
    BR("Brazil", "BRA", 76, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bahamas](http://en.wikipedia.org/wiki/The_Bahamas)
     * [[BS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BS), BHS, 44,
     * Officially assigned]
     */
    BS("Bahamas", "BHS", 44, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bhutan](http://en.wikipedia.org/wiki/Bhutan)
     * [[BT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BT), BTN, 64,
     * Officially assigned]
     */
    BT("Bhutan", "BTN", 64, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Burma](http://en.wikipedia.org/wiki/Burma)
     * [[BU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BU), BUMM, 104,
     * Officially assigned]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `BUR`
     * (not `[BUMM](http://en.wikipedia.org/wiki/ISO_3166-3#BUMM)`).
     *
     *
     * @see .MM
     */
    BU("Burma", "BUR", 104, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Bouvet Island](http://en.wikipedia.org/wiki/Bouvet_Island)
     * [[BV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BV), BVT, 74,
     * Officially assigned]
     */
    BV("Bouvet Island", "BVT", 74, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Botswana](http://en.wikipedia.org/wiki/Botswana)
     * [[BW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BW), BWA, 72,
     * Officially assigned]
     */
    BW("Botswana", "BWA", 72, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Belarus](http://en.wikipedia.org/wiki/Belarus)
     * [[BY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BY), BLR, 112,
     * Officially assigned]
     */
    BY("Belarus", "BLR", 112, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Belize](http://en.wikipedia.org/wiki/Belize)
     * [[BZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BZ), BLZ, 84,
     * Officially assigned]
     */
    BZ("Belize", "BLZ", 84, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Canada](http://en.wikipedia.org/wiki/Canada)
     * [[CA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CA), CAN, 124,
     * Officially assigned]
     */
    CA("Canada", "CAN", 124, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cocos (Keeling) Islands](http://en.wikipedia.org/wiki/Cocos_(Keeling)_Islands)
     * [[CC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CC), CCK, 166,
     * Officially assigned]
     */
    CC("Cocos (Keeling) Islands", "CCK", 166, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Congo, the Democratic Republic of the](http://en.wikipedia.org/wiki/Democratic_Republic_of_the_Congo)
     * [[CD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CD), COD, 180,
     * Officially assigned]
     *
     * @see .ZR
     */
    CD("Congo, the Democratic Republic of the", "COD", 180, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Central African Republic](http://en.wikipedia.org/wiki/Central_African_Republic)
     * [[CF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CF), CAF, 140,
     * Officially assigned]
     */
    CF("Central African Republic", "CAF", 140, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Congo](http://en.wikipedia.org/wiki/Republic_of_the_Congo)
     * [[CG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CG), COG, 178,
     * Officially assigned]
     */
    CG("Congo", "COG", 178, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Switzerland](http://en.wikipedia.org/wiki/Switzerland)
     * [[CH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CH), CHE, 756,
     * Officially assigned]
     */
    CH("Switzerland", "CHE", 756, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cte d'Ivoire](http://en.wikipedia.org/wiki/C%C3%B4te_d%27Ivoire)
     * [[CI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CI), CIV, 384,
     * Officially assigned]
     */
    CI("C\u00F4te d'Ivoire", "CIV", 384, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cook Islands](http://en.wikipedia.org/wiki/Cook_Islands)
     * [[CK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CK), COK, 184,
     * Officially assigned]
     */
    CK("Cook Islands", "COK", 184, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Chile](http://en.wikipedia.org/wiki/Chile)
     * [[CL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CL), CHL, 152,
     * Officially assigned]
     */
    CL("Chile", "CHL", 152, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cameroon](http://en.wikipedia.org/wiki/Cameroon)
     * [[CM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CM), CMR, 120,
     * Officially assigned]
     */
    CM("Cameroon", "CMR", 120, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [China](http://en.wikipedia.org/wiki/China)
     * [[CN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CN), CHN, 156,
     * Officially assigned]
     */
    CN("China", "CHN", 156, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Colombia](http://en.wikipedia.org/wiki/Colombia)
     * [[CO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CO), COL, 170,
     * Officially assigned]
     */
    CO("Colombia", "COL", 170, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Clipperton Island](http://en.wikipedia.org/wiki/Clipperton_Island)
     * [[CP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CP), CPT, -1,
     * Exceptionally reserved]
     */
    CP("Clipperton Island", "CPT", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Costa Rica](http://en.wikipedia.org/wiki/Costa_Rica)
     * [[CR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CR), CRI, 188,
     * Officially assigned]
     */
    CR("Costa Rica", "CRI", 188, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Serbia and Montenegro](http://en.wikipedia.org/wiki/Serbia_and_Montenegro)
     * [[CS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CS), SCG, 891,
     * Traditionally reserved]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `SCG`
     * (not `[CSXX](http://en.wikipedia.org/wiki/ISO_3166-3#CSXX)`).
     *
     */
    CS("Serbia and Montenegro", "SCG", 891, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Cuba](http://en.wikipedia.org/wiki/Cuba)
     * [[CU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CU), CUB, 192,
     * Officially assigned]
     */
    CU("Cuba", "CUB", 192, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cape Verde](http://en.wikipedia.org/wiki/Cape_Verde)
     * [[CV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CV), CPV, 132,
     * Officially assigned]
     */
    CV("Cape Verde", "CPV", 132, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Curaao](http://en.wikipedia.org/wiki/Cura%C3%A7ao)
     * [[CW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CW), CUW, 531,
     * Officially assigned]
     */
    CW("Cura\u00E7ao", "CUW", 531, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Christmas Island](http://en.wikipedia.org/wiki/Christmas_Island)
     * [[CX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CX), CXR, 162,
     * Officially assigned]
     */
    CX("Christmas Island", "CXR", 162, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cyprus](http://en.wikipedia.org/wiki/Cyprus)
     * [[CY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CY), CYP, 196,
     * Officially assigned]
     */
    CY("Cyprus", "CYP", 196, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Czech Republic](http://en.wikipedia.org/wiki/Czech_Republic)
     * [[CZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CZ), CZE, 203,
     * Officially assigned]
     */
    CZ("Czech Republic", "CZE", 203, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Germany](http://en.wikipedia.org/wiki/Germany)
     * [[DE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DE), DEU, 276,
     * Officially assigned]
     */
    DE("Germany", "DEU", 276, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Diego Garcia](http://en.wikipedia.org/wiki/Diego_Garcia)
     * [[DG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DG), DGA, -1,
     * Exceptionally reserved]
     */
    DG("Diego Garcia", "DGA", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Djibouti](http://en.wikipedia.org/wiki/Djibouti)
     * [[DJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DJ), DJI, 262,
     * Officially assigned]
     */
    DJ("Djibouti", "DJI", 262, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Denmark](http://en.wikipedia.org/wiki/Denmark)
     * [[DK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DK), DNK, 208,
     * Officially assigned]
     */
    DK("Denmark", "DNK", 208, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Dominica](http://en.wikipedia.org/wiki/Dominica)
     * [[DM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DM), DMA, 212,
     * Officially assigned]
     */
    DM("Dominica", "DMA", 212, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Dominican Republic](http://en.wikipedia.org/wiki/Dominican_Republic)
     * [[DO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DO), DOM, 214,
     * Officially assigned]
     */
    DO("Dominican Republic", "DOM", 214, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Algeria](http://en.wikipedia.org/wiki/Algeria)
     * [[DZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DZ), DZA, 12,
     * Officially assigned]
     */
    DZ("Algeria", "DZA", 12, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ceuta](http://en.wikipedia.org/wiki/Ceuta),
     * [Melilla](http://en.wikipedia.org/wiki/Melilla)
     * [[EA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EA), null, -1,
     * Exceptionally reserved]
     */
    EA("Ceuta, Melilla", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Ecuador](http://en.wikipedia.org/wiki/Ecuador)
     * [[EC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EC), ECU, 218,
     * Officially assigned]
     */
    EC("Ecuador", "ECU", 218, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Estonia](http://en.wikipedia.org/wiki/Estonia)
     * [[EE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EE), EST, 233,
     * Officially assigned]
     */
    EE("Estonia", "EST", 233, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Egypt](http://en.wikipedia.org/wiki/Egypt)
     * [[EG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EG), EGY, 818,
     * Officially assigned]
     */
    EG("Egypt", "EGY", 818, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Western Sahara](http://en.wikipedia.org/wiki/Western_Sahara)
     * [[EH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EH), ESH, 732,
     * Officially assigned]
     */
    EH("Western Sahara", "ESH", 732, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Eritrea](http://en.wikipedia.org/wiki/Eritrea)
     * [[ER](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ER), ERI, 232,
     * Officially assigned]
     */
    ER("Eritrea", "ERI", 232, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Spain](http://en.wikipedia.org/wiki/Spain)
     * [[ES](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ES), ESP, 724,
     * Officially assigned]
     */
    ES("Spain", "ESP", 724, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ethiopia](http://en.wikipedia.org/wiki/Ethiopia)
     * [[ET](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ET), ETH, 231,
     * Officially assigned]
     */
    ET("Ethiopia", "ETH", 231, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [European Union](http://en.wikipedia.org/wiki/European_Union)
     * [[EU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EU), null, -1,
     * Exceptionally reserved]
     */
    EU("European Union", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Eurozone](http://en.wikipedia.org/wiki/Eurozone)
     * [[EZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EZ), null, -1,
     * Exceptionally reserved]
     *
     * @since 1.23
     */
    EZ("Eurozone", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Finland](http://en.wikipedia.org/wiki/Finland)
     * [[FI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FI), FIN, 246,
     * Officially assigned]
     *
     * @see .SF
     */
    FI("Finland", "FIN", 246, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Fiji](http://en.wikipedia.org/wiki/Fiji)
     * [[FJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#), FJI, 242,
     * Officially assigned]
     */
    FJ("Fiji", "FJI", 242, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Falkland Islands (Malvinas)](http://en.wikipedia.org/wiki/Falkland_Islands)
     * [[FK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FK), FLK, 238,
     * Officially assigned]
     */
    FK("Falkland Islands (Malvinas)", "FLK", 238, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Micronesia, Federated States of](http://en.wikipedia.org/wiki/Federated_States_of_Micronesia)
     * [[FM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FM), FSM, 583,
     * Officially assigned]
     */
    FM("Micronesia, Federated States of", "FSM", 583, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Faroe Islands](http://en.wikipedia.org/wiki/Faroe_Islands)
     * [[FO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FO), FRO, 234,
     * Officially assigned]
     */
    FO("Faroe Islands", "FRO", 234, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [France](http://en.wikipedia.org/wiki/France)
     * [[FR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FR), FRA, 250,
     * Officially assigned]
     */
    FR("France", "FRA", 250, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [France, Metropolitan](http://en.wikipedia.org/wiki/Metropolitan_France)
     * [[FX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FX), FXX, 249,
     * Exceptionally reserved]
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 249.
     *
     */
    FX("France, Metropolitan", "FXX", 249, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Gabon ](http://en.wikipedia.org/wiki/Gabon)
     * [[GA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GA), GAB, 266,
     * Officially assigned]
     */
    GA("Gabon", "GAB", 266, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United Kingdom](http://en.wikipedia.org/wiki/United_Kingdom)
     * [[GB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GB), GBR, 826,
     * Officially assigned]
     *
     * @see .UK
     */
    GB("United Kingdom", "GBR", 826, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Grenada](http://en.wikipedia.org/wiki/Grenada)
     * [[GD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GD), GRD, 308,
     * Officially assigned]
     */
    GD("Grenada", "GRD", 308, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Georgia](http://en.wikipedia.org/wiki/Georgia_(country))
     * [[GE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GE), GEO, 268,
     * Officially assigned]
     */
    GE("Georgia", "GEO", 268, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [French Guiana](http://en.wikipedia.org/wiki/French_Guiana)
     * [[GF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GF), GUF, 254,
     * Officially assigned]
     */
    GF("French Guiana", "GUF", 254, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guernsey](http://en.wikipedia.org/wiki/Guernsey)
     * [[GG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GG), GGY, 831,
     * Officially assigned]
     */
    GG("Guernsey", "GGY", 831, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ghana](http://en.wikipedia.org/wiki/Ghana)
     * [[GH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GH), GHA, 288,
     * Officially assigned]
     */
    GH("Ghana", "GHA", 288, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Gibraltar](http://en.wikipedia.org/wiki/Gibraltar)
     * [[GI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GI), GIB, 292,
     * Officially assigned]
     */
    GI("Gibraltar", "GIB", 292, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Greenland](http://en.wikipedia.org/wiki/Greenland)
     * [[GL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GL), GRL, 304,
     * Officially assigned]
     */
    GL("Greenland", "GRL", 304, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Gambia](http://en.wikipedia.org/wiki/The_Gambia)
     * [[GM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GM), GMB, 270,
     * Officially assigned]
     */
    GM("Gambia", "GMB", 270, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guinea](http://en.wikipedia.org/wiki/Guinea)
     * [[GN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GN), GIN, 324,
     * Officially assigned]
     */
    GN("Guinea", "GIN", 324, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guadeloupe](http://en.wikipedia.org/wiki/Guadeloupe)
     * [[GP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GP), GLP, 312,
     * Officially assigned]
     */
    GP("Guadeloupe", "GLP", 312, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Equatorial Guinea](http://en.wikipedia.org/wiki/Equatorial_Guinea)
     * [[GQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GQ), GNQ, 226,
     * Officially assigned]
     */
    GQ("Equatorial Guinea", "GNQ", 226, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Greece](http://en.wikipedia.org/wiki/Greece)
     * [[GR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GR), GRC, 300,
     * Officially assigned]
     */
    GR("Greece", "GRC", 300, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [South Georgia and the South Sandwich Islands](http://en.wikipedia.org/wiki/South_Georgia_and_the_South_Sandwich_Islands)
     * [[GS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GS), SGS, 239,
     * Officially assigned]
     */
    GS("South Georgia and the South Sandwich Islands", "SGS", 239, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guatemala](http://en.wikipedia.org/wiki/Guatemala)
     * [[GT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GT), GTM, 320,
     * Officially assigned]
     */
    GT("Guatemala", "GTM", 320, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guam](http://en.wikipedia.org/wiki/Guam)
     * [[GU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GU), GUM, 316,
     * Officially assigned]
     */
    GU("Guam", "GUM", 316, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guinea-Bissau](http://en.wikipedia.org/wiki/Guinea-Bissau)
     * [[GW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GW), GNB, 624,
     * Officially assigned]
     */
    GW("Guinea-Bissau", "GNB", 624, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guyana](http://en.wikipedia.org/wiki/Guyana)
     * [[GY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GY), GUY, 328,
     * Officially assigned]
     */
    GY("Guyana", "GUY", 328, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Hong Kong](http://en.wikipedia.org/wiki/Hong_Kong)
     * [[HK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HK), HKG, 344,
     * Officially assigned]
     */
    HK("Hong Kong", "HKG", 344, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Heard Island and McDonald Islands](http://en.wikipedia.org/wiki/Heard_Island_and_McDonald_Islands)
     * [[HM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HM), HMD, 334,
     * Officially assigned]
     */
    HM("Heard Island and McDonald Islands", "HMD", 334, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Honduras](http://en.wikipedia.org/wiki/Honduras)
     * [[HN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HN), HND, 340,
     * Officially assigned]
     */
    HN("Honduras", "HND", 340, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Croatia](http://en.wikipedia.org/wiki/Croatia)
     * [[HR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HR), HRV, 191,
     * Officially assigned]
     */
    HR("Croatia", "HRV", 191, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Haiti](http://en.wikipedia.org/wiki/Haiti)
     * [[HT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HT), HTI, 332,
     * Officially assigned]
     */
    HT("Haiti", "HTI", 332, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Hungary](http://en.wikipedia.org/wiki/Hungary)
     * [[HU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HU), HUN, 348,
     * Officially assigned]
     */
    HU("Hungary", "HUN", 348, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Canary Islands](http://en.wikipedia.org/wiki/Canary_Islands)
     * [[IC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IC), null, -1,
     * Exceptionally reserved]
     */
    IC("Canary Islands", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Indonesia](http://en.wikipedia.org/wiki/Indonesia)
     * [[ID](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ID), IDN, 360,
     * Officially assigned]
     */
    ID("Indonesia", "IDN", 360, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ireland](http://en.wikipedia.org/wiki/Republic_of_Ireland)
     * [[IE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IE), IRL, 372,
     * Officially assigned]
     */
    IE("Ireland", "IRL", 372, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Israel](http://en.wikipedia.org/wiki/Israel)
     * [[IL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IL), ISR, 376,
     * Officially assigned]
     */
    IL("Israel", "ISR", 376, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Isle of Man](http://en.wikipedia.org/wiki/Isle_of_Man)
     * [[IM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IM), IMN, 833,
     * Officially assigned]
     */
    IM("Isle of Man", "IMN", 833, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [India](http://en.wikipedia.org/wiki/India)
     * [[IN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IN), IND, 356,
     * Officially assigned]
     */
    IN("India", "IND", 356, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [British Indian Ocean Territory](http://en.wikipedia.org/wiki/British_Indian_Ocean_Territory)
     * [[IO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IO), IOT, 86,
     * Officially assigned]
     */
    IO("British Indian Ocean Territory", "IOT", 86, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Iraq](http://en.wikipedia.org/wiki/Iraq)
     * [[IQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IQ), IRQ, 368,
     * Officially assigned]
     */
    IQ("Iraq", "IRQ", 368, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Iran, Islamic Republic of](http://en.wikipedia.org/wiki/Iran)
     * [[IR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IR), IRN, 364,
     * Officially assigned]
     */
    IR("Iran, Islamic Republic of", "IRN", 364, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Iceland](http://en.wikipedia.org/wiki/Iceland)
     * [[IS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IS), ISL, 352,
     * Officially assigned]
     */
    IS("Iceland", "ISL", 352, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Italy](http://en.wikipedia.org/wiki/Italy)
     * [[IT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IT), ITA, 380,
     * Officially assigned]
     */
    IT("Italy", "ITA", 380, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Jersey](http://en.wikipedia.org/wiki/Jersey)
     * [[JE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JE), JEY, 832,
     * Officially assigned]
     */
    JE("Jersey", "JEY", 832, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Jamaica](http://en.wikipedia.org/wiki/Jamaica)
     * [[JM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JM), JAM, 388,
     * Officially assigned]
     */
    JM("Jamaica", "JAM", 388, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Jordan](http://en.wikipedia.org/wiki/Jordan)
     * [[JO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JO), JOR, 400,
     * Officially assigned]
     */
    JO("Jordan", "JOR", 400, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Japan](http://en.wikipedia.org/wiki/Japan)
     * [[JP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JP), JPN, 392,
     * Officially assigned]
     */
    JP("Japan", "JPN", 392, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kenya](http://en.wikipedia.org/wiki/Kenya)
     * [[KE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KE), KEN, 404,
     * Officially assigned]
     */
    KE("Kenya", "KEN", 404, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kyrgyzstan](http://en.wikipedia.org/wiki/Kyrgyzstan)
     * [[KG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KG), KGZ, 417,
     * Officially assigned]
     */
    KG("Kyrgyzstan", "KGZ", 417, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cambodia](http://en.wikipedia.org/wiki/Cambodia)
     * [[KH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KH), KHM, 116,
     * Officially assigned]
     */
    KH("Cambodia", "KHM", 116, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kiribati](http://en.wikipedia.org/wiki/Kiribati)
     * [[KI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KI), KIR, 296,
     * Officially assigned]
     */
    KI("Kiribati", "KIR", 296, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Comoros](http://en.wikipedia.org/wiki/Comoros)
     * [[KM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KM), COM, 174,
     * Officially assigned]
     */
    KM("Comoros", "COM", 174, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Kitts and Nevis](http://en.wikipedia.org/wiki/Saint_Kitts_and_Nevis)
     * [[KN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KN), KNA, 659,
     * Officially assigned]
     */
    KN("Saint Kitts and Nevis", "KNA", 659, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Korea, Democratic People's Republic of](http://en.wikipedia.org/wiki/North_Korea)
     * [[KP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KP), PRK, 408,
     * Officially assigned]
     */
    KP("Korea, Democratic People's Republic of", "PRK", 408, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Korea, Republic of](http://en.wikipedia.org/wiki/South_Korea)
     * [[KR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KR), KOR, 410,
     * Officially assigned]
     */
    KR("Korea, Republic of", "KOR", 410, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kuwait](http://en.wikipedia.org/wiki/Kuwait)
     * [[KW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KW), KWT, 414,
     * Officially assigned]
     */
    KW("Kuwait", "KWT", 414, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cayman Islands](http://en.wikipedia.org/wiki/Cayman_Islands)
     * [[KY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KY), CYM, 136,
     * Officially assigned]
     */
    KY("Cayman Islands", "CYM", 136, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kazakhstan](http://en.wikipedia.org/wiki/Kazakhstan)
     * [[KZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KZ), KAZ, 398,
     * Officially assigned]
     */
    KZ("Kazakhstan", "KAZ", 398, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lao People's Democratic Republic](http://en.wikipedia.org/wiki/Laos)
     * [[LA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LA), LAO, 418,
     * Officially assigned]
     */
    LA("Lao People's Democratic Republic", "LAO", 418, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lebanon](http://en.wikipedia.org/wiki/Lebanon)
     * [[LB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LB), LBN, 422,
     * Officially assigned]
     */
    LB("Lebanon", "LBN", 422, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Lucia](http://en.wikipedia.org/wiki/Saint_Lucia)
     * [[LC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LC), LCA, 662,
     * Officially assigned]
     */
    LC("Saint Lucia", "LCA", 662, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Liechtenstein](http://en.wikipedia.org/wiki/Liechtenstein)
     * [[LI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LI), LIE, 438,
     * Officially assigned]
     */
    LI("Liechtenstein", "LIE", 438, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sri Lanka](http://en.wikipedia.org/wiki/Sri_Lanka)
     * [[LK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LK), LKA, 144,
     * Officially assigned]
     */
    LK("Sri Lanka", "LKA", 144, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Liberia](http://en.wikipedia.org/wiki/Liberia)
     * [[LR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LR), LBR, 430,
     * Officially assigned]
     */
    LR("Liberia", "LBR", 430, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lesotho](http://en.wikipedia.org/wiki/Lesotho)
     * [[LS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LS), LSO, 426,
     * Officially assigned]
     */
    LS("Lesotho", "LSO", 426, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lithuania](http://en.wikipedia.org/wiki/Lithuania)
     * [[LT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LT), LTU, 440,
     * Officially assigned]
     */
    LT("Lithuania", "LTU", 440, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Luxembourg](http://en.wikipedia.org/wiki/Luxembourg)
     * [[LU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LU), LUX, 442,
     * Officially assigned]
     */
    LU("Luxembourg", "LUX", 442, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Latvia](http://en.wikipedia.org/wiki/Latvia)
     * [[LV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LV), LVA, 428,
     * Officially assigned]
     */
    LV("Latvia", "LVA", 428, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Libya](http://en.wikipedia.org/wiki/Libya)
     * [[LY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LY), LBY, 434,
     * Officially assigned]
     */
    LY("Libya", "LBY", 434, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Morocco](http://en.wikipedia.org/wiki/Morocco)
     * [[MA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MA), MAR, 504,
     * Officially assigned]
     */
    MA("Morocco", "MAR", 504, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Monaco](http://en.wikipedia.org/wiki/Monaco)
     * [[MC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MC), MCO, 492,
     * Officially assigned]
     */
    MC("Monaco", "MCO", 492, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Moldova, Republic of](http://en.wikipedia.org/wiki/Moldova)
     * [[MD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MD), MDA, 498,
     * Officially assigned]
     */
    MD("Moldova, Republic of", "MDA", 498, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Montenegro](http://en.wikipedia.org/wiki/Montenegro)
     * [[ME](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ME), MNE, 499,
     * Officially assigned]
     */
    ME("Montenegro", "MNE", 499, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Martin (French part)](http://en.wikipedia.org/wiki/Collectivity_of_Saint_Martin)
     * [[MF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MF), MAF, 663,
     * Officially assigned]
     */
    MF("Saint Martin (French part)", "MAF", 663, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Madagascar](http://en.wikipedia.org/wiki/Madagascar)
     * [[MG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MG), MDG, 450,
     * Officially assigned]
     */
    MG("Madagascar", "MDG", 450, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Marshall Islands](http://en.wikipedia.org/wiki/Marshall_Islands)
     * [[MH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MH), MHL, 584,
     * Officially assigned]
     */
    MH("Marshall Islands", "MHL", 584, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [North Macedonia, Republic of](https://en.wikipedia.org/wiki/North_Macedonia)
     * [[MK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MK), MKD, 807,
     * Officially assigned]
     */
    MK("North Macedonia, Republic of", "MKD", 807, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mali](http://en.wikipedia.org/wiki/Mali)
     * [[ML](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ML), MLI, 466,
     * Officially assigned]
     */
    ML("Mali", "MLI", 466, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Myanmar](http://en.wikipedia.org/wiki/Myanmar)
     * [[MM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MM), MMR, 104,
     * Officially assigned]
     *
     * @see .BU
     */
    MM("Myanmar", "MMR", 104, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mongolia](http://en.wikipedia.org/wiki/Mongolia)
     * [[MN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MN), MNG, 496,
     * Officially assigned]
     */
    MN("Mongolia", "MNG", 496, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Macao](http://en.wikipedia.org/wiki/Macau)
     * [[MO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MO), MCO, 492,
     * Officially assigned]
     */
    MO("Macao", "MAC", 446, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Northern Mariana Islands](http://en.wikipedia.org/wiki/Northern_Mariana_Islands)
     * [[MP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MP), MNP, 580,
     * Officially assigned]
     */
    MP("Northern Mariana Islands", "MNP", 580, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Martinique](http://en.wikipedia.org/wiki/Martinique)
     * [[MQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MQ), MTQ, 474,
     * Officially assigned]
     */
    MQ("Martinique", "MTQ", 474, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mauritania](http://en.wikipedia.org/wiki/Mauritania)
     * [[MR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MR), MRT, 478,
     * Officially assigned]
     */
    MR("Mauritania", "MRT", 478, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Montserrat](http://en.wikipedia.org/wiki/Montserrat)
     * [[MS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MS), MSR, 500,
     * Officially assigned]
     */
    MS("Montserrat", "MSR", 500, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Malta](http://en.wikipedia.org/wiki/Malta)
     * [[MT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MT), MLT, 470,
     * Officially assigned]
     */
    MT("Malta", "MLT", 470, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mauritius](http://en.wikipedia.org/wiki/Mauritius)
     * [[MU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MU), MUS, 480,
     * Officially assigned]]
     */
    MU("Mauritius", "MUS", 480, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Maldives](http://en.wikipedia.org/wiki/Maldives)
     * [[MV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MV), MDV, 462,
     * Officially assigned]
     */
    MV("Maldives", "MDV", 462, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Malawi](http://en.wikipedia.org/wiki/Malawi)
     * [[MW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MW), MWI, 454,
     * Officially assigned]
     */
    MW("Malawi", "MWI", 454, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mexico](http://en.wikipedia.org/wiki/Mexico)
     * [[MX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MX), MEX, 484,
     * Officially assigned]
     */
    MX("Mexico", "MEX", 484, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Malaysia](http://en.wikipedia.org/wiki/Malaysia)
     * [[MY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MY), MYS, 458,
     * Officially assigned]
     */
    MY("Malaysia", "MYS", 458, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mozambique](http://en.wikipedia.org/wiki/Mozambique)
     * [[MZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MZ), MOZ, 508,
     * Officially assigned]
     */
    MZ("Mozambique", "MOZ", 508, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Namibia](http://en.wikipedia.org/wiki/Namibia)
     * [[NA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NA), NAM, 516,
     * Officially assigned]
     */
    NA("Namibia", "NAM", 516, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [New Caledonia](http://en.wikipedia.org/wiki/New_Caledonia)
     * [[NC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NC), NCL, 540,
     * Officially assigned]
     */
    NC("New Caledonia", "NCL", 540, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Niger](http://en.wikipedia.org/wiki/Niger)
     * [[NE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NE), NER, 562,
     * Officially assigned]
     */
    NE("Niger", "NER", 562, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Norfolk Island](http://en.wikipedia.org/wiki/Norfolk_Island)
     * [[NF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NF), NFK, 574,
     * Officially assigned]
     */
    NF("Norfolk Island", "NFK", 574, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nigeria](http://en.wikipedia.org/wiki/Nigeria)
     * [[NG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NG), NGA, 566,
     * Officially assigned]
     */
    NG("Nigeria", "NGA", 566, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nicaragua](http://en.wikipedia.org/wiki/Nicaragua)
     * [[NI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NI), NIC, 558,
     * Officially assigned]
     */
    NI("Nicaragua", "NIC", 558, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Netherlands](http://en.wikipedia.org/wiki/Netherlands)
     * [[NL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NL), NLD, 528,
     * Officially assigned]
     */
    NL("Netherlands", "NLD", 528, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Norway](http://en.wikipedia.org/wiki/Norway)
     * [[NO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NO), NOR, 578,
     * Officially assigned]
     */
    NO("Norway", "NOR", 578, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nepal](http://en.wikipedia.org/wiki/Nepal)
     * [[NP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NP), NPL, 524,
     * Officially assigned]
     */
    NP("Nepal", "NPL", 524, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nauru](http://en.wikipedia.org/wiki/Nauru)
     * [[NR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NR), NRU, 520,
     * Officially assigned]
     */
    NR("Nauru", "NRU", 520, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Neutral Zone](http://en.wikipedia.org/wiki/Saudi%E2%80%93Iraqi_neutral_zone)
     * [[NT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NT), NTZ, 536,
     * Traditionally reserved]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `NTZ`
     * (not `[NTHH](http://en.wikipedia.org/wiki/ISO_3166-3#NTHH)`).
     *
     */
    NT("Neutral Zone", "NTZ", 536, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Niue](http://en.wikipedia.org/wiki/Niue)
     * [[NU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NU), NIU, 570,
     * Officially assigned]
     */
    NU("Niue", "NIU", 570, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [New Zealand](http://en.wikipedia.org/wiki/New_Zealand)
     * [[NZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NZ), NZL, 554,
     * Officially assigned]
     */
    NZ("New Zealand", "NZL", 554, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Oman](http://en.wikipedia.org/wiki/Oman"")
     * [[OM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#OM), OMN, 512,
     * Officially assigned]
     */
    OM("Oman", "OMN", 512, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Panama](http://en.wikipedia.org/wiki/Panama)
     * [[PA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PA), PAN, 591,
     * Officially assigned]
     */
    PA("Panama", "PAN", 591, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Peru](http://en.wikipedia.org/wiki/Peru)
     * [[PE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PE), PER, 604,
     * Officially assigned]
     */
    PE("Peru", "PER", 604, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [French Polynesia](http://en.wikipedia.org/wiki/French_Polynesia)
     * [[PF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PF), PYF, 258,
     * Officially assigned]
     */
    PF("French Polynesia", "PYF", 258, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Papua New Guinea](http://en.wikipedia.org/wiki/Papua_New_Guinea)
     * [[PG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PG), PNG, 598,
     * Officially assigned]
     */
    PG("Papua New Guinea", "PNG", 598, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Philippines](http://en.wikipedia.org/wiki/Philippines)
     * [[PH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PH), PHL, 608,
     * Officially assigned]
     */
    PH("Philippines", "PHL", 608, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Pakistan](http://en.wikipedia.org/wiki/Pakistan)
     * [[PK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PK), PAK, 586,
     * Officially assigned]
     */
    PK("Pakistan", "PAK", 586, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Poland](http://en.wikipedia.org/wiki/Poland)
     * [[PL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PL), POL, 616,
     * Officially assigned]
     */
    PL("Poland", "POL", 616, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Pierre and Miquelon](http://en.wikipedia.org/wiki/Saint_Pierre_and_Miquelon)
     * [[PM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PM), SPM, 666,
     * Officially assigned]
     */
    PM("Saint Pierre and Miquelon", "SPM", 666, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Pitcairn](http://en.wikipedia.org/wiki/Pitcairn_Islands)
     * [[PN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PN), PCN, 612,
     * Officially assigned]
     */
    PN("Pitcairn", "PCN", 612, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Puerto Rico](http://en.wikipedia.org/wiki/Puerto_Rico)
     * [[PR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PR), PRI, 630,
     * Officially assigned]
     */
    PR("Puerto Rico", "PRI", 630, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Palestine, State of](http://en.wikipedia.org/wiki/Palestinian_territories)
     * [[PS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PS), PSE, 275,
     * Officially assigned]
     */
    PS("Palestine, State of", "PSE", 275, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Portugal](http://en.wikipedia.org/wiki/Portugal)
     * [[PT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PT), PRT, 620,
     * Officially assigned]
     */
    PT("Portugal", "PRT", 620, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Palau](http://en.wikipedia.org/wiki/Palau)
     * [[PW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PW), PLW, 585,
     * Officially assigned]
     */
    PW("Palau", "PLW", 585, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Paraguay](http://en.wikipedia.org/wiki/Paraguay)
     * [[PY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PY), PRY, 600,
     * Officially assigned]
     */
    PY("Paraguay", "PRY", 600, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Qatar](http://en.wikipedia.org/wiki/Qatar)
     * [[QA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#QA), QAT, 634,
     * Officially assigned]
     */
    QA("Qatar", "QAT", 634, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Runion](http://en.wikipedia.org/wiki/R%C3%A9union)
     * [[RE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RE), REU, 638,
     * Officially assigned]
     */
    RE("R\u00E9union", "REU", 638, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Romania](http://en.wikipedia.org/wiki/Romania)
     * [[RO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RO), ROU, 642,
     * Officially assigned]
     */
    RO("Romania", "ROU", 642, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Serbia](http://en.wikipedia.org/wiki/Serbia)
     * [[RS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RS), SRB, 688,
     * Officially assigned]
     */
    RS("Serbia", "SRB", 688, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Russian Federation](http://en.wikipedia.org/wiki/Russia)
     * [[RU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RU), RUS, 643,
     * Officially assigned]
     */
    RU("Russian Federation", "RUS", 643, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Rwanda](http://en.wikipedia.org/wiki/Rwanda)
     * [[RW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RW), RWA, 646,
     * Officially assigned]
     */
    RW("Rwanda", "RWA", 646, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saudi Arabia](http://en.wikipedia.org/wiki/Saudi_Arabia)
     * [[SA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SA), SAU, 682,
     * Officially assigned]
     */
    SA("Saudi Arabia", "SAU", 682, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Solomon Islands](http://en.wikipedia.org/wiki/Solomon_Islands)
     * [[SB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SB), SLB, 90,
     * Officially assigned]
     */
    SB("Solomon Islands", "SLB", 90, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Seychelles](http://en.wikipedia.org/wiki/Seychelles)
     * [[SC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SC), SYC, 690,
     * Officially assigned]
     */
    SC("Seychelles", "SYC", 690, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sudan](http://en.wikipedia.org/wiki/Sudan)
     * [[SD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SD), SDN, 729,
     * Officially assigned]
     */
    SD("Sudan", "SDN", 729, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sweden](http://en.wikipedia.org/wiki/Sweden)
     * [[SE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SE), SWE, 752,
     * Officially assigned]
     */
    SE("Sweden", "SWE", 752, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Finland](http://en.wikipedia.org/wiki/Finland)
     * [[SF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SF), FIN, 246,
     * Traditionally reserved]
     *
     * @see .FI
     */
    SF("Finland", "FIN", 246, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Singapore](http://en.wikipedia.org/wiki/Singapore)
     * [[SG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SG), SGP, 702,
     * Officially assigned]
     */
    SG("Singapore", "SGP", 702, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Helena, Ascension and Tristan da Cunha](http://en.wikipedia.org/wiki/Saint_Helena,_Ascension_and_Tristan_da_Cunha)
     * [[SH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SH), SHN, 654,
     * Officially assigned]
     */
    SH("Saint Helena, Ascension and Tristan da Cunha", "SHN", 654, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Slovenia](http://en.wikipedia.org/wiki/Slovenia)
     * [[SI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SI), SVN, 705,
     * Officially assigned]
     */
    SI("Slovenia", "SVN", 705, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Svalbard and Jan Mayen](http://en.wikipedia.org/wiki/Svalbard_and_Jan_Mayen)
     * [[SJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SJ), SJM, 744,
     * Officially assigned]
     */
    SJ("Svalbard and Jan Mayen", "SJM", 744, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Slovakia](http://en.wikipedia.org/wiki/Slovakia)
     * [[SK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SK), SVK, 703,
     * Officially assigned]
     */
    SK("Slovakia", "SVK", 703, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sierra Leone](http://en.wikipedia.org/wiki/Sierra_Leone)
     * [[SL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SL), SLE, 694,
     * Officially assigned]
     */
    SL("Sierra Leone", "SLE", 694, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [San Marino](http://en.wikipedia.org/wiki/San_Marino)
     * [[SM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SM), SMR, 674,
     * Officially assigned]
     */
    SM("San Marino", "SMR", 674, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Senegal](http://en.wikipedia.org/wiki/Senegal)
     * [[SN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SN), SEN, 686,
     * Officially assigned]
     */
    SN("Senegal", "SEN", 686, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Somalia](http://en.wikipedia.org/wiki/Somalia)
     * [[SO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SO), SOM, 706,
     * Officially assigned]
     */
    SO("Somalia", "SOM", 706, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Suriname](http://en.wikipedia.org/wiki/Suriname)
     * [[SR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SR), SUR, 740,
     * Officially assigned]
     */
    SR("Suriname", "SUR", 740, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [South Sudan](http://en.wikipedia.org/wiki/South_Sudan)
     * [[SS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SS), SSD, 728,
     * Officially assigned]
     */
    SS("South Sudan", "SSD", 728, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sao Tome and Principe](http://en.wikipedia.org/wiki/S%C3%A3o_Tom%C3%A9_and_Pr%C3%ADncipe)
     * [[ST](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ST), STP, 678,
     * Officially assigned]
     */
    ST("Sao Tome and Principe", "STP", 678, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [USSR](http://en.wikipedia.org/wiki/Soviet_Union)
     * [[SU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SU), SUN, 810,
     * Exceptionally reserved]
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 810.
     *
     */
    SU("USSR", "SUN", 810, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [El Salvador](http://en.wikipedia.org/wiki/El_Salvador)
     * [[SV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SV), SLV, 222,
     * Officially assigned]
     */
    SV("El Salvador", "SLV", 222, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sint Maarten (Dutch part)](http://en.wikipedia.org/wiki/Sint_Maarten)
     * [[SX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SX), SXM, 534,
     * Officially assigned]
     */
    SX("Sint Maarten (Dutch part)", "SXM", 534, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Syrian Arab Republic](http://en.wikipedia.org/wiki/Syria)
     * [[SY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SY), SYR, 760,
     * Officially assigned]
     */
    SY("Syrian Arab Republic", "SYR", 760, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Swaziland](http://en.wikipedia.org/wiki/Swaziland)
     * [[SZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SZ), SWZ, 748,
     * Officially assigned]
     */
    SZ("Swaziland", "SWZ", 748, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tristan da Cunha](http://en.wikipedia.org/wiki/Tristan_da_Cunha)
     * [[TA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TA), TAA, -1,
     * Exceptionally reserved.
     */
    TA("Tristan da Cunha", "TAA", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Turks and Caicos Islands](http://en.wikipedia.org/wiki/Turks_and_Caicos_Islands)
     * [[TC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TC), TCA, 796,
     * Officially assigned]
     */
    TC("Turks and Caicos Islands", "TCA", 796, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Chad](http://en.wikipedia.org/wiki/Chad)
     * [[TD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TD), TCD, 148,
     * Officially assigned]
     */
    TD("Chad", "TCD", 148, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [French Southern Territories](http://en.wikipedia.org/wiki/French_Southern_and_Antarctic_Lands)
     * [[TF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TF), ATF, 260,
     * Officially assigned]
     */
    TF("French Southern Territories", "ATF", 260, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Togo](http://en.wikipedia.org/wiki/Togo)
     * [[TG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TG), TGO, 768,
     * Officially assigned]
     */
    TG("Togo", "TGO", 768, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Thailand](http://en.wikipedia.org/wiki/Thailand)
     * [[TH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TH), THA, 764,
     * Officially assigned]
     */
    TH("Thailand", "THA", 764, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tajikistan](http://en.wikipedia.org/wiki/Tajikistan)
     * [[TJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TJ), TJK, 762,
     * Officially assigned]
     */
    TJ("Tajikistan", "TJK", 762, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tokelau](http://en.wikipedia.org/wiki/Tokelau)
     * [[TK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TK), TKL, 772,
     * Officially assigned]
     */
    TK("Tokelau", "TKL", 772, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Timor-Leste](http://en.wikipedia.org/wiki/East_Timor)
     * [[TL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TL), TLS, 626,
     * Officially assigned]
     *
     * @see .TM
     */
    TL("Timor-Leste", "TLS", 626, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Turkmenistan](http://en.wikipedia.org/wiki/Turkmenistan)
     * [[TM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TM), TKM, 795,
     * Officially assigned]
     */
    TM("Turkmenistan", "TKM", 795, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tunisia](http://en.wikipedia.org/wiki/Tunisia)
     * [[TN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TN), TUN, 788,
     * Officially assigned]
     */
    TN("Tunisia", "TUN", 788, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tonga](http://en.wikipedia.org/wiki/Tonga)
     * [[TO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TO), TON, 776,
     * Officially assigned]
     */
    TO("Tonga", "TON", 776, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [East Timor](http://en.wikipedia.org/wiki/East_Timor)
     * [[TP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TP), TMP, 626,
     * Traditionally reserved]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `TMP`
     * (not `[TPTL](http://en.wikipedia.org/wiki/ISO_3166-3#TPTL)`).
     *
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 626.
     *
     *
     * @see .TL
     */
    TP("East Timor", "TMP", 626, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Turkey](http://en.wikipedia.org/wiki/Turkey)
     * [[TR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TR), TUR, 792,
     * Officially assigned]
     */
    TR("Turkey", "TUR", 792, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Trinidad and Tobago](http://en.wikipedia.org/wiki/Trinidad_and_Tobago)
     * [[TT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TT), TTO, 780,
     * Officially assigned]
     */
    TT("Trinidad and Tobago", "TTO", 780, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tuvalu](http://en.wikipedia.org/wiki/Tuvalu)
     * [[TV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TV), TUV, 798,
     * Officially assigned]
     */
    TV("Tuvalu", "TUV", 798, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Taiwan, Province of China](http://en.wikipedia.org/wiki/Taiwan)
     * [[TW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TW), TWN, 158,
     * Officially assigned]
     */
    TW("Taiwan, Province of China", "TWN", 158, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tanzania, United Republic of](http://en.wikipedia.org/wiki/Tanzania)
     * [[TZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TZ), TZA, 834,
     * Officially assigned]
     */
    TZ("Tanzania, United Republic of", "TZA", 834, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ukraine](http://en.wikipedia.org/wiki/Ukraine)
     * [[UA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UA), UKR, 804,
     * Officially assigned]
     */
    UA("Ukraine", "UKR", 804, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Uganda](http://en.wikipedia.org/wiki/Uganda)
     * [[UG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UG), UGA, 800,
     * Officially assigned]
     */
    UG("Uganda", "UGA", 800, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United Kingdom](http://en.wikipedia.org/wiki/United_Kingdom)
     * [[UK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UK), null, 826,
     * Exceptionally reserved]
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 826.
     *
     *
     * @see .GB
     */
    UK("United Kingdom", null, 826, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [United States Minor Outlying Islands](http://en.wikipedia.org/wiki/United_States_Minor_Outlying_Islands)
     * [[UM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UM), UMI, 581,
     * Officially assigned]
     */
    UM("United States Minor Outlying Islands", "UMI", 581, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United States](http://en.wikipedia.org/wiki/United_States)
     * [[US](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#US), USA, 840,
     * Officially assigned]
     */
    US("United States", "USA", 840, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Uruguay](http://en.wikipedia.org/wiki/Uruguay)
     * [[UY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UY), URY, 858,
     * Officially assigned]
     */
    UY("Uruguay", "URY", 858, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Uzbekistan](http://en.wikipedia.org/wiki/Uzbekistan)
     * [[UZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UZ), UZB, 860,
     * Officially assigned]
     */
    UZ("Uzbekistan", "UZB", 860, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Holy See (Vatican City State)](http://en.wikipedia.org/wiki/Vatican_City)
     * [[VA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VA), VAT, 336,
     * Officially assigned]
     */
    VA("Holy See (Vatican City State)", "VAT", 336, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Vincent and the Grenadines](http://en.wikipedia.org/wiki/Saint_Vincent_and_the_Grenadines)
     * [[VC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VC), VCT, 670,
     * Officially assigned]
     */
    VC("Saint Vincent and the Grenadines", "VCT", 670, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Venezuela, Bolivarian Republic of](http://en.wikipedia.org/wiki/Venezuela)
     * [[VE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VE), VEN, 862,
     * Officially assigned]
     */
    VE("Venezuela, Bolivarian Republic of", "VEN", 862, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Virgin Islands, British](http://en.wikipedia.org/wiki/British_Virgin_Islands)
     * [[VG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VG), VGB, 92,
     * Officially assigned]
     */
    VG("Virgin Islands, British", "VGB", 92, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Virgin Islands, U.S.](http://en.wikipedia.org/wiki/United_States_Virgin_Islands)
     * [[VI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VI), VIR, 850,
     * Officially assigned]
     */
    VI("Virgin Islands, U.S.", "VIR", 850, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Viet Nam](http://en.wikipedia.org/wiki/Vietnam)
     * [[VN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VN), VNM, 704,
     * Officially assigned]
     */
    VN("Viet Nam", "VNM", 704, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Vanuatu](http://en.wikipedia.org/wiki/Vanuatu)
     * [[VU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VU), VUT, 548,
     * Officially assigned]
     */
    VU("Vanuatu", "VUT", 548, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Wallis and Futuna](http://en.wikipedia.org/wiki/Wallis_and_Futuna)
     * [[WF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#WF), WLF, 876,
     * Officially assigned]
     */
    WF("Wallis and Futuna", "WLF", 876, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Samoa](http://en.wikipedia.org/wiki/Samoa)
     * [[WS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#WS), WSM, 882,
     * Officially assigned]
     */
    WS("Samoa", "WSM", 882, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kosovo, Republic of](http://en.wikipedia.org/wiki/Kosovo)
     * [[XK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#XK), XXK, -1,
     * User assigned]
     */
    XK("Kosovo, Republic of", "XKX", -1, Assignment.USER_ASSIGNED),

    /**
     * [Yemen](http://en.wikipedia.org/wiki/Yemen)
     * [[YE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#YE), YEM, 887,
     * Officially assigned]
     */
    YE("Yemen", "YEM", 887, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mayotte](http://en.wikipedia.org/wiki/Mayotte)
     * [[YT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#YT), MYT, 175,
     * Officially assigned]
     */
    YT("Mayotte", "MYT", 175, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Yugoslavia](http://en.wikipedia.org/wiki/Yugoslavia)
     * [[YU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#YU), YUG, 890,
     * Traditionally reserved]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `YUG`
     * (not `[YUCS](http://en.wikipedia.org/wiki/ISO_3166-3#YUCS)`).
     *
     */
    YU("Yugoslavia", "YUG", 890, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [South Africa](http://en.wikipedia.org/wiki/South_Africa)
     * [[ZA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZA), ZAF, 710,
     * Officially assigned]
     */
    ZA("South Africa", "ZAF", 710, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Zambia](http://en.wikipedia.org/wiki/Zambia)
     * [[ZM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZM), ZMB, 894,
     * Officially assigned]
     */
    ZM("Zambia", "ZMB", 894, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Zaire](http://en.wikipedia.org/wiki/Zaire)
     * [[ZR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZR), ZAR, 180,
     * Traditionally reserved]
     *
     *
     *
     * Since version 1.16, the value of alpha-3 code of this entry is `ZAR`
     * (not `[ZRCD](http://en.wikipedia.org/wiki/ISO_3166-3#ZRCD)`).
     *
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 180.
     *
     *
     * @see .CD
     */
    ZR("Zaire", "ZAR", 180, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Zimbabwe](http://en.wikipedia.org/wiki/Zimbabwe)
     * [[ZW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZW), ZWE, 716,
     * Officially assigned]
     */
    ZW("Zimbabwe", "ZWE", 716, Assignment.OFFICIALLY_ASSIGNED);

    /**
     * Code assignment state in [ISO 3166-1](http://en.wikipedia.org/wiki/ISO_3166-1).
     *
     * @see [Decoding table of ISO 3166-1 alpha-2 codes](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2.Decoding_table)
     */
    enum class Assignment {
        /**
         * [Officially assigned](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Officially_assigned_code_elements).
         *
         * Assigned to a country, territory, or area of geographical interest.
         */
        OFFICIALLY_ASSIGNED,

        /**
         * [User assigned](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#User-assigned_code_elements).
         *
         * Free for assignment at the disposal of users.
         */
        USER_ASSIGNED,

        /**
         * [Exceptionally reserved](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Exceptional_reservations).
         *
         * Reserved on request for restricted use.
         */
        EXCEPTIONALLY_RESERVED,

        /**
         * [Transitionally reserved](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Transitional_reservations).
         *
         * Deleted from ISO 3166-1 but reserved transitionally.
         */
        TRANSITIONALLY_RESERVED,

        /**
         * [Indeterminately reserved](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Indeterminate_reservations).
         *
         * Used in coding systems associated with ISO 3166-1.
         */
        INDETERMINATELY_RESERVED,

        /**
         * [Not used](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#Codes_currently_agreed_not_to_use).
         *
         * Not used in ISO 3166-1 in deference to international property
         * organization names.
         */
        NOT_USED
    }
}