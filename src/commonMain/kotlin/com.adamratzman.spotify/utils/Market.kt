/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2021; Original author: Adam Ratzman */
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
 * In addition, there are static methods to get a `Market` instance that
 * corresponds to a given alpha-2/alpha-3/numeric code ([.getByCode],
 * [.getByCode]).
 *
 *
 * <pre style="background-color: #EEEEEE; margin-left: 2em; margin-right: 2em; border: 1px solid black; padding: 0.5em;">
 * <span style="color: darkgreen;">// List all the country codes.</span>
 * for (Market code : Market.values())
 * {
 * <span style="color: darkgreen;">// For example, "[US] United States" is printed.</span>
 * System.out.format(<span style="color: darkred;">"[%s] %s\n"</span>, code, code.[.getName]);
 * }
 *
 * <span style="color: darkgreen;">// Get a Market instance by ISO 3166-1 code.</span>
 * Market code = Market.[getByCode][.getByCode](<span style="color: darkred;">"JP"</span>);
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
 *
 * <span style="color: darkgreen;">// Get a Market by a Locale instance.</span>
 * code = Market.[getByLocale][.getByLocale](locale);
 *
 * <span style="color: darkgreen;">// Get the currency of the country.</span>
 *
 * <span style="color: darkgreen;">// Get a list by a regular expression for names.
 * //
 * // The list will contain:
 * //
 * //     Market.AE ed Arab Emirates
 * //     Market.GB ed Kingdom
 * //     Market.TZ : Tanzania, United Republic of
 * //     Market.UK ed Kingdom
 * //     Market.UM ed States Minor Outlying Islands
 * //     Market.US ed States
 * //</span>
 * List&lt;Market&gt; list = Market.[findByName][.findByName](<span style="color: darkred;">".*United.*"</span>);
 *
 * <span style="color: darkgreen;">
 * // For backward compatibility for older versions than 1.16, some
 * // 4-letter ISO 3166-3 codes are accepted by getByCode(String, boolean)
 * // and its variants. To be concrete:
 * //
 * //     [ANHH](https://en.wikipedia.org/wiki/ISO_3166-3#ANHH) : Market.AN
 * //     [BUMM](https://en.wikipedia.org/wiki/ISO_3166-3#BUMM) : Market.BU
 * //     [CSXX](https://en.wikipedia.org/wiki/ISO_3166-3#CSXX) : Market.CS
 * //     [NTHH](https://en.wikipedia.org/wiki/ISO_3166-3#NTHH) : Market.NT
 * //     [TPTL](https://en.wikipedia.org/wiki/ISO_3166-3#TPTL) : Market.TP
 * //     [YUCS](https://en.wikipedia.org/wiki/ISO_3166-3#YUCS) : Market.YU
 * //     [ZRCD](https://en.wikipedia.org/wiki/ISO_3166-3#ZRCD) : Market.ZR
 * //</span>
 * code = Market.[getByCode][.getByCode](<span style="color: darkred;">"ANHH"</span>);
</pre> *
 *
 * @author Takahiko Kawasaki (https://github.com/TakahikoKawasaki/nv-i18n)
 */
public enum class Market(
    /**
     * Get the country name.
     *
     * @return
     * The country name.
     */
    public val marketName: String,
    /**
     * Get the [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) code.
     *
     * @return
     * The [ISO 3166-1 alpha-3](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-3) code.
     * Some country codes reserved exceptionally (such as [.EU])
     * returns `null`.
     */
    public val alpha3: String?,
    /**
     * Get the [ISO 3166-1 numeric](http://en.wikipedia.org/wiki/ISO_3166-1_numeric) code.
     *
     * @return
     * The [ISO 3166-1 numeric](http://en.wikipedia.org/wiki/ISO_3166-1_numeric) code.
     * Country codes reserved exceptionally (such as [.EU])
     * returns `-1`.
     */
    public val numeric: Int,
    /**
     * Get the assignment state of this country code in ISO 3166-1.
     *
     * @return
     * The assignment state.
     *
     * @see [Decoding table of ISO 3166-1 alpha-2 codes](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2.Decoding_table)
     */
    public val assignment: Assignment
) {
    /**
     * [Ascension Island](http://en.wikipedia.org/wiki/Ascension_Island)
     * [Market Code: AC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AC)
     */
    AC("Ascension Island", "ASC", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Andorra](http://en.wikipedia.org/wiki/Andorra)
     * [Market Code: AD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AD)
     */
    AD("Andorra", "AND", 20, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United Arab Emirates](http://en.wikipedia.org/wiki/United_Arab_Emirates)
     * [Market Code: AE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AE)
     */
    AE("United Arab Emirates", "ARE", 784, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Afghanistan](http://en.wikipedia.org/wiki/Afghanistan)
     * [Market Code: AF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AF)
     */
    AF("Afghanistan", "AFG", 4, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Antigua and Barbuda](http://en.wikipedia.org/wiki/Antigua_and_Barbuda)
     * [Market Code: AG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AG)
     */
    AG("Antigua and Barbuda", "ATG", 28, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Anguilla](http://en.wikipedia.org/wiki/Anguilla)
     * [Market Code: AI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AI)
     */
    AI("Anguilla", "AIA", 660, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Albania](http://en.wikipedia.org/wiki/Albania)
     * [AL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AL), ALB, 8,
     * Officially assigned]
     */
    AL("Albania", "ALB", 8, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Armenia](http://en.wikipedia.org/wiki/Armenia)
     * [Market Code: AM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AM)
     */
    AM("Armenia", "ARM", 51, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Netherlands Antilles](http://en.wikipedia.org/wiki/Netherlands_Antilles)
     * [Market Code: AN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AN)
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
     * [Market Code: AO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AO)
     */
    AO("Angola", "AGO", 24, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Antarctica](http://en.wikipedia.org/wiki/Antarctica)
     * [Market Code: AQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AQ)
     */
    AQ("Antarctica", "ATA", 10, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Argentina](http://en.wikipedia.org/wiki/Argentina)
     * [Market Code: AR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AR)
     */
    AR("Argentina", "ARG", 32, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [American Samoa](http://en.wikipedia.org/wiki/American_Samoa)
     * [Market Code: AS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AS)
     */
    AS("American Samoa", "ASM", 16, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Austria](http://en.wikipedia.org/wiki/Austria)
     * [Market Code: AT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AT)
     */
    AT("Austria", "AUT", 40, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Australia](http://en.wikipedia.org/wiki/Australia)
     * [Market Code: AU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AU)
     */
    AU("Australia", "AUS", 36, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Aruba](http://en.wikipedia.org/wiki/Aruba)
     * [Market Code: AW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AW)
     */
    AW("Aruba", "ABW", 533, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [land Islands](http://en.wikipedia.org/wiki/%C3%85land_Islands)
     * [Market Code: AX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AX)
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
     * [Market Code: AZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#AZ)
     */
    AZ("Azerbaijan", "AZE", 31, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bosnia and Herzegovina](http://en.wikipedia.org/wiki/Bosnia_and_Herzegovina)
     * [Market Code: BA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BA)
     */
    BA("Bosnia and Herzegovina", "BIH", 70, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Barbados](http://en.wikipedia.org/wiki/Barbados)
     * [Market Code: BB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BB)
     */
    BB("Barbados", "BRB", 52, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bangladesh](http://en.wikipedia.org/wiki/Bangladesh)
     * [Market Code: BD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BD)
     */
    BD("Bangladesh", "BGD", 50, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Belgium](http://en.wikipedia.org/wiki/Belgium)
     * [Market Code: BE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BE)
     */
    BE("Belgium", "BEL", 56, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Burkina Faso](http://en.wikipedia.org/wiki/Burkina_Faso)
     * [Market Code: BF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BF)
     */
    BF("Burkina Faso", "BFA", 854, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bulgaria](http://en.wikipedia.org/wiki/Bulgaria)
     * [Market Code: BG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BG)
     */
    BG("Bulgaria", "BGR", 100, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bahrain](http://en.wikipedia.org/wiki/Bahrain)
     * [Market Code: BH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BH)
     */
    BH("Bahrain", "BHR", 48, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Burundi](http://en.wikipedia.org/wiki/Burundi)
     * [Market Code: BI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BI)
     */
    BI("Burundi", "BDI", 108, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Benin](http://en.wikipedia.org/wiki/Benin)
     * [Market Code: BJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BJ)
     */
    BJ("Benin", "BEN", 204, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Barthlemy](http://en.wikipedia.org/wiki/Saint_Barth%C3%A9lemy)
     * [Market Code: BL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BL)
     */
    BL("Saint Barth\u00E9lemy", "BLM", 652, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bermuda](http://en.wikipedia.org/wiki/Bermuda)
     * [Market Code: BM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BM)
     */
    BM("Bermuda", "BMU", 60, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Brunei Darussalam](http://en.wikipedia.org/wiki/Brunei)
     * [Market Code: BN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BN)
     */
    BN("Brunei Darussalam", "BRN", 96, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bolivia, Plurinational State of](http://en.wikipedia.org/wiki/Bolivia)
     * [Market Code: BO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BO)
     */
    BO("Bolivia, Plurinational State of", "BOL", 68, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bonaire, Sint Eustatius and Saba](http://en.wikipedia.org/wiki/Caribbean_Netherlands)
     * [Market Code: BQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BQ)
     */
    BQ("Bonaire, Sint Eustatius and Saba", "BES", 535, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Brazil](http://en.wikipedia.org/wiki/Brazil)
     * [Market Code: BR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BR)
     */
    BR("Brazil", "BRA", 76, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bahamas](http://en.wikipedia.org/wiki/The_Bahamas)
     * [Market Code: BS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BS)
     */
    BS("Bahamas", "BHS", 44, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Bhutan](http://en.wikipedia.org/wiki/Bhutan)
     * [Market Code: BT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BT)
     */
    BT("Bhutan", "BTN", 64, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Burma](http://en.wikipedia.org/wiki/Burma)
     * [Market Code: BU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BU)
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
     * [Market Code: BV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BV)
     */
    BV("Bouvet Island", "BVT", 74, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Botswana](http://en.wikipedia.org/wiki/Botswana)
     * [Market Code: BW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BW)
     */
    BW("Botswana", "BWA", 72, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Belarus](http://en.wikipedia.org/wiki/Belarus)
     * [Market Code: BY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BY)
     */
    BY("Belarus", "BLR", 112, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Belize](http://en.wikipedia.org/wiki/Belize)
     * [Market Code: BZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#BZ)
     */
    BZ("Belize", "BLZ", 84, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Canada](http://en.wikipedia.org/wiki/Canada)
     * [Market Code: CA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CA)
     */
    CA("Canada", "CAN", 124, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cocos (Keeling) Islands](http://en.wikipedia.org/wiki/Cocos_(Keeling)_Islands)
     * [Market Code: CC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CC)
     */
    CC("Cocos (Keeling) Islands", "CCK", 166, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Congo, the Democratic Republic of the](http://en.wikipedia.org/wiki/Democratic_Republic_of_the_Congo)
     * [Market Code: CD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CD)
     *
     * @see .ZR
     */
    CD("Congo, the Democratic Republic of the", "COD", 180, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Central African Republic](http://en.wikipedia.org/wiki/Central_African_Republic)
     * [Market Code: CF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CF)
     */
    CF("Central African Republic", "CAF", 140, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Congo](http://en.wikipedia.org/wiki/Republic_of_the_Congo)
     * [Market Code: CG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CG)
     */
    CG("Congo", "COG", 178, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Switzerland](http://en.wikipedia.org/wiki/Switzerland)
     * [Market Code: CH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CH)
     */
    CH("Switzerland", "CHE", 756, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cte d'Ivoire](http://en.wikipedia.org/wiki/C%C3%B4te_d%27Ivoire)
     * [Market Code: CI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CI)
     */
    CI("C\u00F4te d'Ivoire", "CIV", 384, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cook Islands](http://en.wikipedia.org/wiki/Cook_Islands)
     * [Market Code: CK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CK)
     */
    CK("Cook Islands", "COK", 184, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Chile](http://en.wikipedia.org/wiki/Chile)
     * [Market Code: CL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CL)
     */
    CL("Chile", "CHL", 152, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cameroon](http://en.wikipedia.org/wiki/Cameroon)
     * [Market Code: CM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CM)
     */
    CM("Cameroon", "CMR", 120, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [China](http://en.wikipedia.org/wiki/China)
     * [Market Code: CN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CN)
     */
    CN("China", "CHN", 156, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Colombia](http://en.wikipedia.org/wiki/Colombia)
     * [Market Code: CO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CO)
     */
    CO("Colombia", "COL", 170, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Clipperton Island](http://en.wikipedia.org/wiki/Clipperton_Island)
     * [Market Code: CP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CP)
     */
    CP("Clipperton Island", "CPT", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Costa Rica](http://en.wikipedia.org/wiki/Costa_Rica)
     * [Market Code: CR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CR)
     */
    CR("Costa Rica", "CRI", 188, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Serbia and Montenegro](http://en.wikipedia.org/wiki/Serbia_and_Montenegro)
     * [Market Code: CS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CS)
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
     * [Market Code: CU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CU)
     */
    CU("Cuba", "CUB", 192, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cape Verde](http://en.wikipedia.org/wiki/Cape_Verde)
     * [Market Code: CV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CV)
     */
    CV("Cape Verde", "CPV", 132, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Curaao](http://en.wikipedia.org/wiki/Cura%C3%A7ao)
     * [Market Code: CW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CW)
     */
    CW("Cura\u00E7ao", "CUW", 531, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Christmas Island](http://en.wikipedia.org/wiki/Christmas_Island)
     * [Market Code: CX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CX)
     */
    CX("Christmas Island", "CXR", 162, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cyprus](http://en.wikipedia.org/wiki/Cyprus)
     * [Market Code: CY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CY)
     */
    CY("Cyprus", "CYP", 196, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Czech Republic](http://en.wikipedia.org/wiki/Czech_Republic)
     * [Market Code: CZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#CZ)
     */
    CZ("Czech Republic", "CZE", 203, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Germany](http://en.wikipedia.org/wiki/Germany)
     * [Market Code: DE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DE)
     */
    DE("Germany", "DEU", 276, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Diego Garcia](http://en.wikipedia.org/wiki/Diego_Garcia)
     * [Market Code: DG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DG)
     */
    DG("Diego Garcia", "DGA", -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Djibouti](http://en.wikipedia.org/wiki/Djibouti)
     * [Market Code: DJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DJ)
     */
    DJ("Djibouti", "DJI", 262, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Denmark](http://en.wikipedia.org/wiki/Denmark)
     * [Market Code: DK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DK)
     */
    DK("Denmark", "DNK", 208, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Dominica](http://en.wikipedia.org/wiki/Dominica)
     * [Market Code: DM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DM)
     */
    DM("Dominica", "DMA", 212, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Dominican Republic](http://en.wikipedia.org/wiki/Dominican_Republic)
     * [Market Code: DO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DO)
     */
    DO("Dominican Republic", "DOM", 214, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Algeria](http://en.wikipedia.org/wiki/Algeria)
     * [Market Code: DZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#DZ)
     */
    DZ("Algeria", "DZA", 12, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ceuta](http://en.wikipedia.org/wiki/Ceuta),
     * [Melilla](http://en.wikipedia.org/wiki/Melilla)
     * [Market Code: EA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EA)
     */
    EA("Ceuta, Melilla", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Ecuador](http://en.wikipedia.org/wiki/Ecuador)
     * [Market Code: EC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EC)
     */
    EC("Ecuador", "ECU", 218, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Estonia](http://en.wikipedia.org/wiki/Estonia)
     * [Market Code: EE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EE)
     */
    EE("Estonia", "EST", 233, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Egypt](http://en.wikipedia.org/wiki/Egypt)
     * [Market Code: EG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EG)
     */
    EG("Egypt", "EGY", 818, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Western Sahara](http://en.wikipedia.org/wiki/Western_Sahara)
     * [Market Code: EH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EH)
     */
    EH("Western Sahara", "ESH", 732, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Eritrea](http://en.wikipedia.org/wiki/Eritrea)
     * [Market Code: ER](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ER)
     */
    ER("Eritrea", "ERI", 232, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Spain](http://en.wikipedia.org/wiki/Spain)
     * [Market Code: ES](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ES)
     */
    ES("Spain", "ESP", 724, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ethiopia](http://en.wikipedia.org/wiki/Ethiopia)
     * [Market Code: ET](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ET)
     */
    ET("Ethiopia", "ETH", 231, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [European Union](http://en.wikipedia.org/wiki/European_Union)
     * [Market Code: EU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EU)
     */
    EU("European Union", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Eurozone](http://en.wikipedia.org/wiki/Eurozone)
     * [Market Code: EZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#EZ)
     *
     * @since 1.23
     */
    EZ("Eurozone", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Finland](http://en.wikipedia.org/wiki/Finland)
     * [Market Code: FI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FI)
     *
     * @see .SF
     */
    FI("Finland", "FIN", 246, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Fiji](http://en.wikipedia.org/wiki/Fiji)
     * [Market Code: FJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#)
     */
    FJ("Fiji", "FJI", 242, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Falkland Islands (Malvinas)](http://en.wikipedia.org/wiki/Falkland_Islands)
     * [Market Code: FK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FK)
     */
    FK("Falkland Islands (Malvinas)", "FLK", 238, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Micronesia, Federated States of](http://en.wikipedia.org/wiki/Federated_States_of_Micronesia)
     * [Market Code: FM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FM)
     */
    FM("Micronesia, Federated States of", "FSM", 583, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Faroe Islands](http://en.wikipedia.org/wiki/Faroe_Islands)
     * [Market Code: FO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FO)
     */
    FO("Faroe Islands", "FRO", 234, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [France](http://en.wikipedia.org/wiki/France)
     * [Market Code: FR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FR)
     */
    FR("France", "FRA", 250, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [France, Metropolitan](http://en.wikipedia.org/wiki/Metropolitan_France)
     * [Market Code: FX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#FX)
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 249.
     *
     */
    FX("France, Metropolitan", "FXX", 249, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Gabon ](http://en.wikipedia.org/wiki/Gabon)
     * [Market Code: GA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GA)
     */
    GA("Gabon", "GAB", 266, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United Kingdom](http://en.wikipedia.org/wiki/United_Kingdom)
     * [Market Code: GB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GB)
     *
     * @see .UK
     */
    GB("United Kingdom", "GBR", 826, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Grenada](http://en.wikipedia.org/wiki/Grenada)
     * [Market Code: GD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GD)
     */
    GD("Grenada", "GRD", 308, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Georgia](http://en.wikipedia.org/wiki/Georgia_(country))
     * [Market Code: GE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GE)
     */
    GE("Georgia", "GEO", 268, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [French Guiana](http://en.wikipedia.org/wiki/French_Guiana)
     * [Market Code: GF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GF)
     */
    GF("French Guiana", "GUF", 254, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guernsey](http://en.wikipedia.org/wiki/Guernsey)
     * [Market Code: GG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GG)
     */
    GG("Guernsey", "GGY", 831, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ghana](http://en.wikipedia.org/wiki/Ghana)
     * [Market Code: GH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GH)
     */
    GH("Ghana", "GHA", 288, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Gibraltar](http://en.wikipedia.org/wiki/Gibraltar)
     * [Market Code: GI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GI)
     */
    GI("Gibraltar", "GIB", 292, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Greenland](http://en.wikipedia.org/wiki/Greenland)
     * [Market Code: GL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GL)
     */
    GL("Greenland", "GRL", 304, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Gambia](http://en.wikipedia.org/wiki/The_Gambia)
     * [Market Code: GM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GM)
     */
    GM("Gambia", "GMB", 270, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guinea](http://en.wikipedia.org/wiki/Guinea)
     * [Market Code: GN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GN)
     */
    GN("Guinea", "GIN", 324, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guadeloupe](http://en.wikipedia.org/wiki/Guadeloupe)
     * [Market Code: GP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GP)
     */
    GP("Guadeloupe", "GLP", 312, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Equatorial Guinea](http://en.wikipedia.org/wiki/Equatorial_Guinea)
     * [Market Code: GQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GQ)
     */
    GQ("Equatorial Guinea", "GNQ", 226, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Greece](http://en.wikipedia.org/wiki/Greece)
     * [Market Code: GR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GR)
     */
    GR("Greece", "GRC", 300, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [South Georgia and the South Sandwich Islands](http://en.wikipedia.org/wiki/South_Georgia_and_the_South_Sandwich_Islands)
     * [Market Code: GS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GS)
     */
    GS("South Georgia and the South Sandwich Islands", "SGS", 239, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guatemala](http://en.wikipedia.org/wiki/Guatemala)
     * [Market Code: GT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GT)
     */
    GT("Guatemala", "GTM", 320, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guam](http://en.wikipedia.org/wiki/Guam)
     * [Market Code: GU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GU)
     */
    GU("Guam", "GUM", 316, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guinea-Bissau](http://en.wikipedia.org/wiki/Guinea-Bissau)
     * [Market Code: GW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GW)
     */
    GW("Guinea-Bissau", "GNB", 624, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Guyana](http://en.wikipedia.org/wiki/Guyana)
     * [Market Code: GY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#GY)
     */
    GY("Guyana", "GUY", 328, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Hong Kong](http://en.wikipedia.org/wiki/Hong_Kong)
     * [Market Code: HK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HK)
     */
    HK("Hong Kong", "HKG", 344, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Heard Island and McDonald Islands](http://en.wikipedia.org/wiki/Heard_Island_and_McDonald_Islands)
     * [Market Code: HM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HM)
     */
    HM("Heard Island and McDonald Islands", "HMD", 334, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Honduras](http://en.wikipedia.org/wiki/Honduras)
     * [Market Code: HN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HN)
     */
    HN("Honduras", "HND", 340, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Croatia](http://en.wikipedia.org/wiki/Croatia)
     * [Market Code: HR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HR)
     */
    HR("Croatia", "HRV", 191, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Haiti](http://en.wikipedia.org/wiki/Haiti)
     * [Market Code: HT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HT)
     */
    HT("Haiti", "HTI", 332, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Hungary](http://en.wikipedia.org/wiki/Hungary)
     * [Market Code: HU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#HU)
     */
    HU("Hungary", "HUN", 348, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Canary Islands](http://en.wikipedia.org/wiki/Canary_Islands)
     * [Market Code: IC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IC)
     */
    IC("Canary Islands", null, -1, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [Indonesia](http://en.wikipedia.org/wiki/Indonesia)
     * [Market Code: ID](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ID)
     */
    ID("Indonesia", "IDN", 360, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ireland](http://en.wikipedia.org/wiki/Republic_of_Ireland)
     * [Market Code: IE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IE)
     */
    IE("Ireland", "IRL", 372, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Israel](http://en.wikipedia.org/wiki/Israel)
     * [Market Code: IL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IL)
     */
    IL("Israel", "ISR", 376, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Isle of Man](http://en.wikipedia.org/wiki/Isle_of_Man)
     * [Market Code: IM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IM)
     */
    IM("Isle of Man", "IMN", 833, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [India](http://en.wikipedia.org/wiki/India)
     * [Market Code: IN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IN)
     */
    IN("India", "IND", 356, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [British Indian Ocean Territory](http://en.wikipedia.org/wiki/British_Indian_Ocean_Territory)
     * [Market Code: IO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IO)
     */
    IO("British Indian Ocean Territory", "IOT", 86, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Iraq](http://en.wikipedia.org/wiki/Iraq)
     * [Market Code: IQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IQ)
     */
    IQ("Iraq", "IRQ", 368, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Iran, Islamic Republic of](http://en.wikipedia.org/wiki/Iran)
     * [Market Code: IR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IR)
     */
    IR("Iran, Islamic Republic of", "IRN", 364, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Iceland](http://en.wikipedia.org/wiki/Iceland)
     * [Market Code: IS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IS)
     */
    IS("Iceland", "ISL", 352, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Italy](http://en.wikipedia.org/wiki/Italy)
     * [Market Code: IT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#IT)
     */
    IT("Italy", "ITA", 380, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Jersey](http://en.wikipedia.org/wiki/Jersey)
     * [Market Code: JE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JE)
     */
    JE("Jersey", "JEY", 832, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Jamaica](http://en.wikipedia.org/wiki/Jamaica)
     * [Market Code: JM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JM)
     */
    JM("Jamaica", "JAM", 388, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Jordan](http://en.wikipedia.org/wiki/Jordan)
     * [Market Code: JO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JO)
     */
    JO("Jordan", "JOR", 400, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Japan](http://en.wikipedia.org/wiki/Japan)
     * [Market Code: JP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#JP)
     */
    JP("Japan", "JPN", 392, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kenya](http://en.wikipedia.org/wiki/Kenya)
     * [Market Code: KE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KE)
     */
    KE("Kenya", "KEN", 404, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kyrgyzstan](http://en.wikipedia.org/wiki/Kyrgyzstan)
     * [Market Code: KG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KG)
     */
    KG("Kyrgyzstan", "KGZ", 417, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cambodia](http://en.wikipedia.org/wiki/Cambodia)
     * [Market Code: KH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KH)
     */
    KH("Cambodia", "KHM", 116, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kiribati](http://en.wikipedia.org/wiki/Kiribati)
     * [Market Code: KI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KI)
     */
    KI("Kiribati", "KIR", 296, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Comoros](http://en.wikipedia.org/wiki/Comoros)
     * [Market Code: KM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KM)
     */
    KM("Comoros", "COM", 174, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Kitts and Nevis](http://en.wikipedia.org/wiki/Saint_Kitts_and_Nevis)
     * [Market Code: KN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KN)
     */
    KN("Saint Kitts and Nevis", "KNA", 659, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Korea, Democratic People's Republic of](http://en.wikipedia.org/wiki/North_Korea)
     * [Market Code: KP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KP)
     */
    KP("Korea, Democratic People's Republic of", "PRK", 408, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Korea, Republic of](http://en.wikipedia.org/wiki/South_Korea)
     * [Market Code: KR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KR)
     */
    KR("Korea, Republic of", "KOR", 410, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kuwait](http://en.wikipedia.org/wiki/Kuwait)
     * [Market Code: KW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KW)
     */
    KW("Kuwait", "KWT", 414, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Cayman Islands](http://en.wikipedia.org/wiki/Cayman_Islands)
     * [Market Code: KY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KY)
     */
    KY("Cayman Islands", "CYM", 136, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kazakhstan](http://en.wikipedia.org/wiki/Kazakhstan)
     * [Market Code: KZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#KZ)
     */
    KZ("Kazakhstan", "KAZ", 398, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lao People's Democratic Republic](http://en.wikipedia.org/wiki/Laos)
     * [Market Code: LA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LA)
     */
    LA("Lao People's Democratic Republic", "LAO", 418, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lebanon](http://en.wikipedia.org/wiki/Lebanon)
     * [Market Code: LB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LB)
     */
    LB("Lebanon", "LBN", 422, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Lucia](http://en.wikipedia.org/wiki/Saint_Lucia)
     * [Market Code: LC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LC)
     */
    LC("Saint Lucia", "LCA", 662, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Liechtenstein](http://en.wikipedia.org/wiki/Liechtenstein)
     * [Market Code: LI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LI)
     */
    LI("Liechtenstein", "LIE", 438, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sri Lanka](http://en.wikipedia.org/wiki/Sri_Lanka)
     * [Market Code: LK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LK)
     */
    LK("Sri Lanka", "LKA", 144, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Liberia](http://en.wikipedia.org/wiki/Liberia)
     * [Market Code: LR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LR)
     */
    LR("Liberia", "LBR", 430, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lesotho](http://en.wikipedia.org/wiki/Lesotho)
     * [Market Code: LS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LS)
     */
    LS("Lesotho", "LSO", 426, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Lithuania](http://en.wikipedia.org/wiki/Lithuania)
     * [Market Code: LT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LT)
     */
    LT("Lithuania", "LTU", 440, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Luxembourg](http://en.wikipedia.org/wiki/Luxembourg)
     * [Market Code: LU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LU)
     */
    LU("Luxembourg", "LUX", 442, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Latvia](http://en.wikipedia.org/wiki/Latvia)
     * [Market Code: LV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LV)
     */
    LV("Latvia", "LVA", 428, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Libya](http://en.wikipedia.org/wiki/Libya)
     * [Market Code: LY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#LY)
     */
    LY("Libya", "LBY", 434, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Morocco](http://en.wikipedia.org/wiki/Morocco)
     * [Market Code: MA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MA)
     */
    MA("Morocco", "MAR", 504, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Monaco](http://en.wikipedia.org/wiki/Monaco)
     * [Market Code: MC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MC)
     */
    MC("Monaco", "MCO", 492, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Moldova, Republic of](http://en.wikipedia.org/wiki/Moldova)
     * [Market Code: MD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MD)
     */
    MD("Moldova, Republic of", "MDA", 498, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Montenegro](http://en.wikipedia.org/wiki/Montenegro)
     * [Market Code: ME](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ME)
     */
    ME("Montenegro", "MNE", 499, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Martin (French part)](http://en.wikipedia.org/wiki/Collectivity_of_Saint_Martin)
     * [Market Code: MF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MF)
     */
    MF("Saint Martin (French part)", "MAF", 663, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Madagascar](http://en.wikipedia.org/wiki/Madagascar)
     * [Market Code: MG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MG)
     */
    MG("Madagascar", "MDG", 450, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Marshall Islands](http://en.wikipedia.org/wiki/Marshall_Islands)
     * [Market Code: MH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MH)
     */
    MH("Marshall Islands", "MHL", 584, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [North Macedonia, Republic of](https://en.wikipedia.org/wiki/North_Macedonia)
     * [Market Code: MK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MK)
     */
    MK("North Macedonia, Republic of", "MKD", 807, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mali](http://en.wikipedia.org/wiki/Mali)
     * [Market Code: ML](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ML)
     */
    ML("Mali", "MLI", 466, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Myanmar](http://en.wikipedia.org/wiki/Myanmar)
     * [Market Code: MM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MM)
     *
     * @see .BU
     */
    MM("Myanmar", "MMR", 104, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mongolia](http://en.wikipedia.org/wiki/Mongolia)
     * [Market Code: MN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MN)
     */
    MN("Mongolia", "MNG", 496, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Macao](http://en.wikipedia.org/wiki/Macau)
     * [Market Code: MO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MO)
     */
    MO("Macao", "MAC", 446, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Northern Mariana Islands](http://en.wikipedia.org/wiki/Northern_Mariana_Islands)
     * [Market Code: MP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MP)
     */
    MP("Northern Mariana Islands", "MNP", 580, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Martinique](http://en.wikipedia.org/wiki/Martinique)
     * [Market Code: MQ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MQ)
     */
    MQ("Martinique", "MTQ", 474, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mauritania](http://en.wikipedia.org/wiki/Mauritania)
     * [Market Code: MR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MR)
     */
    MR("Mauritania", "MRT", 478, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Montserrat](http://en.wikipedia.org/wiki/Montserrat)
     * [Market Code: MS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MS)
     */
    MS("Montserrat", "MSR", 500, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Malta](http://en.wikipedia.org/wiki/Malta)
     * [Market Code: MT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MT)
     */
    MT("Malta", "MLT", 470, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mauritius](http://en.wikipedia.org/wiki/Mauritius)
     * [Market Code: MU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MU)
     */
    MU("Mauritius", "MUS", 480, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Maldives](http://en.wikipedia.org/wiki/Maldives)
     * [Market Code: MV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MV)
     */
    MV("Maldives", "MDV", 462, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Malawi](http://en.wikipedia.org/wiki/Malawi)
     * [Market Code: MW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MW)
     */
    MW("Malawi", "MWI", 454, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mexico](http://en.wikipedia.org/wiki/Mexico)
     * [Market Code: MX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MX)
     */
    MX("Mexico", "MEX", 484, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Malaysia](http://en.wikipedia.org/wiki/Malaysia)
     * [Market Code: MY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MY)
     */
    MY("Malaysia", "MYS", 458, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mozambique](http://en.wikipedia.org/wiki/Mozambique)
     * [Market Code: MZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#MZ)
     */
    MZ("Mozambique", "MOZ", 508, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Namibia](http://en.wikipedia.org/wiki/Namibia)
     * [Market Code: NA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NA)
     */
    NA("Namibia", "NAM", 516, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [New Caledonia](http://en.wikipedia.org/wiki/New_Caledonia)
     * [Market Code: NC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NC)
     */
    NC("New Caledonia", "NCL", 540, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Niger](http://en.wikipedia.org/wiki/Niger)
     * [Market Code: NE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NE)
     */
    NE("Niger", "NER", 562, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Norfolk Island](http://en.wikipedia.org/wiki/Norfolk_Island)
     * [Market Code: NF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NF)
     */
    NF("Norfolk Island", "NFK", 574, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nigeria](http://en.wikipedia.org/wiki/Nigeria)
     * [Market Code: NG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NG)
     */
    NG("Nigeria", "NGA", 566, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nicaragua](http://en.wikipedia.org/wiki/Nicaragua)
     * [Market Code: NI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NI)
     */
    NI("Nicaragua", "NIC", 558, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Netherlands](http://en.wikipedia.org/wiki/Netherlands)
     * [Market Code: NL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NL)
     */
    NL("Netherlands", "NLD", 528, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Norway](http://en.wikipedia.org/wiki/Norway)
     * [Market Code: NO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NO)
     */
    NO("Norway", "NOR", 578, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nepal](http://en.wikipedia.org/wiki/Nepal)
     * [Market Code: NP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NP)
     */
    NP("Nepal", "NPL", 524, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Nauru](http://en.wikipedia.org/wiki/Nauru)
     * [Market Code: NR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NR)
     */
    NR("Nauru", "NRU", 520, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Neutral Zone](http://en.wikipedia.org/wiki/Saudi%E2%80%93Iraqi_neutral_zone)
     * [Market Code: NT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NT)
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
     * [Market Code: NU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NU)
     */
    NU("Niue", "NIU", 570, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [New Zealand](http://en.wikipedia.org/wiki/New_Zealand)
     * [Market Code: NZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#NZ)
     */
    NZ("New Zealand", "NZL", 554, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Oman](http://en.wikipedia.org/wiki/Oman"")
     * [Market Code: OM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#OM)
     */
    OM("Oman", "OMN", 512, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Panama](http://en.wikipedia.org/wiki/Panama)
     * [Market Code: PA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PA)
     */
    PA("Panama", "PAN", 591, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Peru](http://en.wikipedia.org/wiki/Peru)
     * [Market Code: PE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PE)
     */
    PE("Peru", "PER", 604, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [French Polynesia](http://en.wikipedia.org/wiki/French_Polynesia)
     * [Market Code: PF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PF)
     */
    PF("French Polynesia", "PYF", 258, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Papua New Guinea](http://en.wikipedia.org/wiki/Papua_New_Guinea)
     * [Market Code: PG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PG)
     */
    PG("Papua New Guinea", "PNG", 598, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Philippines](http://en.wikipedia.org/wiki/Philippines)
     * [Market Code: PH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PH)
     */
    PH("Philippines", "PHL", 608, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Pakistan](http://en.wikipedia.org/wiki/Pakistan)
     * [Market Code: PK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PK)
     */
    PK("Pakistan", "PAK", 586, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Poland](http://en.wikipedia.org/wiki/Poland)
     * [Market Code: PL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PL)
     */
    PL("Poland", "POL", 616, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Pierre and Miquelon](http://en.wikipedia.org/wiki/Saint_Pierre_and_Miquelon)
     * [Market Code: PM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PM)
     */
    PM("Saint Pierre and Miquelon", "SPM", 666, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Pitcairn](http://en.wikipedia.org/wiki/Pitcairn_Islands)
     * [Market Code: PN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PN)
     */
    PN("Pitcairn", "PCN", 612, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Puerto Rico](http://en.wikipedia.org/wiki/Puerto_Rico)
     * [Market Code: PR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PR)
     */
    PR("Puerto Rico", "PRI", 630, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Palestine, State of](http://en.wikipedia.org/wiki/Palestinian_territories)
     * [Market Code: PS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PS)
     */
    PS("Palestine, State of", "PSE", 275, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Portugal](http://en.wikipedia.org/wiki/Portugal)
     * [Market Code: PT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PT)
     */
    PT("Portugal", "PRT", 620, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Palau](http://en.wikipedia.org/wiki/Palau)
     * [Market Code: PW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PW)
     */
    PW("Palau", "PLW", 585, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Paraguay](http://en.wikipedia.org/wiki/Paraguay)
     * [Market Code: PY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#PY)
     */
    PY("Paraguay", "PRY", 600, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Qatar](http://en.wikipedia.org/wiki/Qatar)
     * [Market Code: QA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#QA)
     */
    QA("Qatar", "QAT", 634, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Runion](http://en.wikipedia.org/wiki/R%C3%A9union)
     * [Market Code: RE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RE)
     */
    RE("R\u00E9union", "REU", 638, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Romania](http://en.wikipedia.org/wiki/Romania)
     * [Market Code: RO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RO)
     */
    RO("Romania", "ROU", 642, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Serbia](http://en.wikipedia.org/wiki/Serbia)
     * [Market Code: RS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RS)
     */
    RS("Serbia", "SRB", 688, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Russian Federation](http://en.wikipedia.org/wiki/Russia)
     * [Market Code: RU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RU)
     */
    RU("Russian Federation", "RUS", 643, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Rwanda](http://en.wikipedia.org/wiki/Rwanda)
     * [Market Code: RW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#RW)
     */
    RW("Rwanda", "RWA", 646, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saudi Arabia](http://en.wikipedia.org/wiki/Saudi_Arabia)
     * [Market Code: SA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SA)
     */
    SA("Saudi Arabia", "SAU", 682, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Solomon Islands](http://en.wikipedia.org/wiki/Solomon_Islands)
     * [Market Code: SB](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SB)
     */
    SB("Solomon Islands", "SLB", 90, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Seychelles](http://en.wikipedia.org/wiki/Seychelles)
     * [Market Code: SC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SC)
     */
    SC("Seychelles", "SYC", 690, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sudan](http://en.wikipedia.org/wiki/Sudan)
     * [Market Code: SD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SD)
     */
    SD("Sudan", "SDN", 729, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sweden](http://en.wikipedia.org/wiki/Sweden)
     * [Market Code: SE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SE)
     */
    SE("Sweden", "SWE", 752, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Finland](http://en.wikipedia.org/wiki/Finland)
     * [Market Code: SF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SF)
     *
     * @see .FI
     */
    SF("Finland", "FIN", 246, Assignment.TRANSITIONALLY_RESERVED),

    /**
     * [Singapore](http://en.wikipedia.org/wiki/Singapore)
     * [Market Code: SG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SG)
     */
    SG("Singapore", "SGP", 702, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Helena, Ascension and Tristan da Cunha](http://en.wikipedia.org/wiki/Saint_Helena,_Ascension_and_Tristan_da_Cunha)
     * [Market Code: SH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SH)
     */
    SH("Saint Helena, Ascension and Tristan da Cunha", "SHN", 654, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Slovenia](http://en.wikipedia.org/wiki/Slovenia)
     * [Market Code: SI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SI)
     */
    SI("Slovenia", "SVN", 705, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Svalbard and Jan Mayen](http://en.wikipedia.org/wiki/Svalbard_and_Jan_Mayen)
     * [Market Code: SJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SJ)
     */
    SJ("Svalbard and Jan Mayen", "SJM", 744, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Slovakia](http://en.wikipedia.org/wiki/Slovakia)
     * [Market Code: SK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SK)
     */
    SK("Slovakia", "SVK", 703, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sierra Leone](http://en.wikipedia.org/wiki/Sierra_Leone)
     * [Market Code: SL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SL)
     */
    SL("Sierra Leone", "SLE", 694, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [San Marino](http://en.wikipedia.org/wiki/San_Marino)
     * [Market Code: SM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SM)
     */
    SM("San Marino", "SMR", 674, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Senegal](http://en.wikipedia.org/wiki/Senegal)
     * [Market Code: SN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SN)
     */
    SN("Senegal", "SEN", 686, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Somalia](http://en.wikipedia.org/wiki/Somalia)
     * [Market Code: SO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SO)
     */
    SO("Somalia", "SOM", 706, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Suriname](http://en.wikipedia.org/wiki/Suriname)
     * [Market Code: SR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SR)
     */
    SR("Suriname", "SUR", 740, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [South Sudan](http://en.wikipedia.org/wiki/South_Sudan)
     * [Market Code: SS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SS)
     */
    SS("South Sudan", "SSD", 728, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sao Tome and Principe](http://en.wikipedia.org/wiki/S%C3%A3o_Tom%C3%A9_and_Pr%C3%ADncipe)
     * [Market Code: ST](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ST)
     */
    ST("Sao Tome and Principe", "STP", 678, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [USSR](http://en.wikipedia.org/wiki/Soviet_Union)
     * [Market Code: SU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SU)
     *
     *
     *
     * Since version 1.17, the numeric code of this entry is 810.
     *
     */
    SU("USSR", "SUN", 810, Assignment.EXCEPTIONALLY_RESERVED),

    /**
     * [El Salvador](http://en.wikipedia.org/wiki/El_Salvador)
     * [Market Code: SV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SV)
     */
    SV("El Salvador", "SLV", 222, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Sint Maarten (Dutch part)](http://en.wikipedia.org/wiki/Sint_Maarten)
     * [Market Code: SX](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SX)
     */
    SX("Sint Maarten (Dutch part)", "SXM", 534, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Syrian Arab Republic](http://en.wikipedia.org/wiki/Syria)
     * [Market Code: SY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SY)
     */
    SY("Syrian Arab Republic", "SYR", 760, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Swaziland](http://en.wikipedia.org/wiki/Swaziland)
     * [Market Code: SZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#SZ)
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
     * [Market Code: TC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TC)
     */
    TC("Turks and Caicos Islands", "TCA", 796, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Chad](http://en.wikipedia.org/wiki/Chad)
     * [Market Code: TD](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TD)
     */
    TD("Chad", "TCD", 148, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [French Southern Territories](http://en.wikipedia.org/wiki/French_Southern_and_Antarctic_Lands)
     * [Market Code: TF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TF)
     */
    TF("French Southern Territories", "ATF", 260, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Togo](http://en.wikipedia.org/wiki/Togo)
     * [Market Code: TG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TG)
     */
    TG("Togo", "TGO", 768, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Thailand](http://en.wikipedia.org/wiki/Thailand)
     * [Market Code: TH](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TH)
     */
    TH("Thailand", "THA", 764, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tajikistan](http://en.wikipedia.org/wiki/Tajikistan)
     * [Market Code: TJ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TJ)
     */
    TJ("Tajikistan", "TJK", 762, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tokelau](http://en.wikipedia.org/wiki/Tokelau)
     * [Market Code: TK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TK)
     */
    TK("Tokelau", "TKL", 772, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Timor-Leste](http://en.wikipedia.org/wiki/East_Timor)
     * [Market Code: TL](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TL)
     *
     * @see .TM
     */
    TL("Timor-Leste", "TLS", 626, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Turkmenistan](http://en.wikipedia.org/wiki/Turkmenistan)
     * [Market Code: TM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TM)
     */
    TM("Turkmenistan", "TKM", 795, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tunisia](http://en.wikipedia.org/wiki/Tunisia)
     * [Market Code: TN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TN)
     */
    TN("Tunisia", "TUN", 788, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tonga](http://en.wikipedia.org/wiki/Tonga)
     * [Market Code: TO](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TO)
     */
    TO("Tonga", "TON", 776, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [East Timor](http://en.wikipedia.org/wiki/East_Timor)
     * [Market Code: TP](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TP)
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
     * [Market Code: TR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TR)
     */
    TR("Turkey", "TUR", 792, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Trinidad and Tobago](http://en.wikipedia.org/wiki/Trinidad_and_Tobago)
     * [Market Code: TT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TT)
     */
    TT("Trinidad and Tobago", "TTO", 780, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tuvalu](http://en.wikipedia.org/wiki/Tuvalu)
     * [Market Code: TV](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TV)
     */
    TV("Tuvalu", "TUV", 798, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Taiwan, Province of China](http://en.wikipedia.org/wiki/Taiwan)
     * [Market Code: TW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TW)
     */
    TW("Taiwan, Province of China", "TWN", 158, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Tanzania, United Republic of](http://en.wikipedia.org/wiki/Tanzania)
     * [Market Code: TZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#TZ)
     */
    TZ("Tanzania, United Republic of", "TZA", 834, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Ukraine](http://en.wikipedia.org/wiki/Ukraine)
     * [Market Code: UA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UA)
     */
    UA("Ukraine", "UKR", 804, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Uganda](http://en.wikipedia.org/wiki/Uganda)
     * [Market Code: UG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UG)
     */
    UG("Uganda", "UGA", 800, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United Kingdom](http://en.wikipedia.org/wiki/United_Kingdom)
     * [Market Code: UK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UK)
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
     * [Market Code: UM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UM)
     */
    UM("United States Minor Outlying Islands", "UMI", 581, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [United States](http://en.wikipedia.org/wiki/United_States)
     * [Market Code: US](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#US)
     */
    US("United States", "USA", 840, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Uruguay](http://en.wikipedia.org/wiki/Uruguay)
     * [Market Code: UY](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UY)
     */
    UY("Uruguay", "URY", 858, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Uzbekistan](http://en.wikipedia.org/wiki/Uzbekistan)
     * [Market Code: UZ](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#UZ)
     */
    UZ("Uzbekistan", "UZB", 860, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Holy See (Vatican City State)](http://en.wikipedia.org/wiki/Vatican_City)
     * [Market Code: VA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VA)
     */
    VA("Holy See (Vatican City State)", "VAT", 336, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Saint Vincent and the Grenadines](http://en.wikipedia.org/wiki/Saint_Vincent_and_the_Grenadines)
     * [Market Code: VC](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VC)
     */
    VC("Saint Vincent and the Grenadines", "VCT", 670, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Venezuela, Bolivarian Republic of](http://en.wikipedia.org/wiki/Venezuela)
     * [Market Code: VE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VE)
     */
    VE("Venezuela, Bolivarian Republic of", "VEN", 862, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Virgin Islands, British](http://en.wikipedia.org/wiki/British_Virgin_Islands)
     * [Market Code: VG](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VG)
     */
    VG("Virgin Islands, British", "VGB", 92, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Virgin Islands, U.S.](http://en.wikipedia.org/wiki/United_States_Virgin_Islands)
     * [Market Code: VI](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VI)
     */
    VI("Virgin Islands, U.S.", "VIR", 850, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Viet Nam](http://en.wikipedia.org/wiki/Vietnam)
     * [Market Code: VN](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VN)
     */
    VN("Viet Nam", "VNM", 704, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Vanuatu](http://en.wikipedia.org/wiki/Vanuatu)
     * [Market Code: VU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#VU)
     */
    VU("Vanuatu", "VUT", 548, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Wallis and Futuna](http://en.wikipedia.org/wiki/Wallis_and_Futuna)
     * [Market Code: WF](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#WF)
     */
    WF("Wallis and Futuna", "WLF", 876, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Samoa](http://en.wikipedia.org/wiki/Samoa)
     * [Market Code: WS](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#WS)
     */
    WS("Samoa", "WSM", 882, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Kosovo, Republic of](http://en.wikipedia.org/wiki/Kosovo)
     * [Market Code: XK](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#XK)
     */
    XK("Kosovo, Republic of", "XKX", -1, Assignment.USER_ASSIGNED),

    /**
     * [Yemen](http://en.wikipedia.org/wiki/Yemen)
     * [Market Code: YE](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#YE)
     */
    YE("Yemen", "YEM", 887, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Mayotte](http://en.wikipedia.org/wiki/Mayotte)
     * [Market Code: YT](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#YT)
     */
    YT("Mayotte", "MYT", 175, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Yugoslavia](http://en.wikipedia.org/wiki/Yugoslavia)
     * [Market Code: YU](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#YU)
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
     * [Market Code: ZA](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZA)
     */
    ZA("South Africa", "ZAF", 710, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Zambia](http://en.wikipedia.org/wiki/Zambia)
     * [Market Code: ZM](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZM)
     */
    ZM("Zambia", "ZMB", 894, Assignment.OFFICIALLY_ASSIGNED),

    /**
     * [Zaire](http://en.wikipedia.org/wiki/Zaire)
     * [Market Code: ZR](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZR)
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
     * [Market Code: ZW](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2#ZW)
     */
    ZW("Zimbabwe", "ZWE", 716, Assignment.OFFICIALLY_ASSIGNED);

    /**
     * Code assignment state in [ISO 3166-1](http://en.wikipedia.org/wiki/ISO_3166-1).
     *
     * @see [Decoding table of ISO 3166-1 alpha-2 codes](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2.Decoding_table)
     */
    public enum class Assignment {
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
