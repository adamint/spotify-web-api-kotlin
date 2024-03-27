/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

/**
 * Locale code.
 *
 *
 *
 * The list of the enum entries was generated based on the output from
 * [ Locale.getAvailableLocales()][java.util.Locale.getAvailableLocales] of Java SE 7, but locales whose
 * format do not match either 'xx' or 'xx-XX' were excluded.
 *
 *
 * <pre style="background-color: #EEEEEE; margin-left: 2em; margin-right: 2em; border: 1px solid black;">
 * <span style="color: darkgreen;">// List all the locale codes.</span>
 * for (LocaleCode code : LocaleCode.values())
 * {
 * String language = code.[.getLanguage].[getName()][Language.GEtName];
 * String country  = code.[.getCountry] != null
 * ? code.[.getCountry].[getName()][Market.getName] : null;
 *
 * <span style="color: darkgreen;">// For example, "[de-DE] German, Germany" is printed.</span>
 * System.out.format(<span style="color: darkred;">"[%s] %s, %s\n"</span>, code, language, country);
 * }
 *
 * <span style="color: darkgreen;">// Get a LocaleCode instance in various ways.</span>
 * LocaleCode code;
 * code = LocaleCode.[getByCode][.getByCode](<span style="color: darkred;">"en-GB"</span>);
 * code = LocaleCode.[getByCode][.getByCode](<span style="color: darkred;">"es"</span>, <span style="color: darkred;">"ES"</span>);
 * code = LocaleCode.[getByLocale][.getByLocale](new Locale(<span style="color: darkred;">"pt"</span>, <span style="color: darkred;">"BR"</span>));
 *
 * <span style="color: darkgreen;">// Convert to a Locale instance.</span>
 * Locale locale = LocaleCode.[.en].[.toLocale];
 *
 * <span style="color: darkgreen;">// toLocale() of some LocaleCode instances does not create</span>
 * <span style="color: darkgreen;">// a new Locale instance but returns a static variable of</span>
 * <span style="color: darkgreen;">// Locale class instead. See [.toLocale] for details.</span>
 * if (locale == Locale.ENGLISH)
 * {
 * System.out.println(<span style="color: darkred;">"Locale.en.toLocale() returned Locale.ENGLISH"</span>);
 * }
 *
 * <span style="color: darkgreen;">// Get a list of LocaleCode instances whose language is Arabic.</span>
 * List&lt;LocaleCode&gt; list = LocaleCode.[getByLanguage][.getByLanguage]([LanguageCode.ar]);
 *
 * <span style="color: darkgreen;">// Get a list of LocaleCode instances whose country is Switzerland.</span>
 * list = LocaleCode.[getByCountry][.getByCountry]([Market.CH]);
</pre> *
 *
 * @author Takahiko Kawasaki
 */
public enum class Locale(public val language: Language, public val country: Market) {
    /**
     * [Arabic][Language.AR]
     */

    /**
     * [Arabic][Language.AR], [United Arab Emirates][Market.AE]
     */
    AR_AE(Language.AR, Market.AE),

    /**
     * [Arabic][Language.AR], [Bahrain][Market.BH]
     */
    AR_BH(Language.AR, Market.BH),

    /**
     * [Arabic][Language.AR], [Algeria][Market.DZ]
     */
    AR_DZ(Language.AR, Market.DZ),

    /**
     * [Arabic][Language.AR], [Egypt][Market.EG]
     */
    AR_EG(Language.AR, Market.EG),

    /**
     * [Arabic][Language.AR], [Iraq][Market.IQ]
     */
    AR_IQ(Language.AR, Market.IQ),

    /**
     * [Arabic][Language.AR], [Jordan][Market.JO]
     */
    AR_JO(Language.AR, Market.JO),

    /**
     * [Arabic][Language.AR], [Kuwait][Market.KW]
     */
    AR_KW(Language.AR, Market.KW),

    /**
     * [Arabic][Language.AR], [Lebanon][Market.LB]
     */
    AR_LB(Language.AR, Market.LB),

    /**
     * [Arabic][Language.AR], [Libya][Market.LY]
     */
    AR_LY(Language.AR, Market.LY),

    /**
     * [Arabic][Language.AR], [Morocco][Market.MA]
     */
    AR_MA(Language.AR, Market.MA),

    /**
     * [Arabic][Language.AR], [Oman][Market.OM]
     */
    AR_OM(Language.AR, Market.OM),

    /**
     * [Arabic][Language.AR], [Qatar][Market.QA]
     */
    AR_QA(Language.AR, Market.QA),

    /**
     * [Arabic][Language.AR], [Saudi Arabia][Market.SA]
     */
    AR_SA(Language.AR, Market.SA),

    /**
     * [Arabic][Language.AR], [Sudan][Market.SD]
     */
    AR_SD(Language.AR, Market.SD),

    /**
     * [Arabic][Language.AR], [Syrian Arab Republic][Market.SY]
     */
    AR_SY(Language.AR, Market.SY),

    /**
     * [Arabic][Language.AR], [Tunisia][Market.TN]
     */
    AR_TN(Language.AR, Market.TN),

    /**
     * [Arabic][Language.AR], [Yemen][Market.YE]
     */
    AR_YE(Language.AR, Market.YE),
    /**
     * [Belarusian][Language.BE]
     */

    /**
     * [Belarusian][Language.BE], [Belarus][Market.BY]
     */
    BE_BY(Language.BE, Market.BY),
    /**
     * [Bulgarian][Language.BG]
     */

    /**
     * [Bulgarian][Language.BG], [Bulgaria][Market.BG]
     */
    BG_BG(Language.BG, Market.BG),
    /**
     * [Catalan][Language.CA]
     */

    /**
     * [Catalan][Language.CA], [Spain][Market.ES]
     */
    CA_ES(Language.CA, Market.ES),
    /**
     * [Czech][Language.CS]
     */

    /**
     * [Czech][Language.CS], [Czech Republic][Market.CZ]
     */
    CS_CZ(Language.CS, Market.CZ),
    /**
     * [Danish][Language.DA]
     */

    /**
     * [Danish][Language.DA], [Denmark][Market.DK]
     */
    DA_DK(Language.DA, Market.DK),
    /**
     * [German][Language.DE]
     */

    /**
     * [German][Language.DE], [Austria][Market.AT]
     */
    DE_AT(Language.DE, Market.AT),

    /**
     * [German][Language.DE], [Switzerland][Market.CH]
     */
    DE_CH(Language.DE, Market.CH),

    /**
     * [German][Language.DE], [Germany][Market.DE]
     */
    DE_DE(Language.DE, Market.DE),

    /**
     * [German][Language.DE], [Luxembourg][Market.LU]
     */
    DE_LU(Language.DE, Market.LU),
    /**
     * [Greek][Language.EL]
     */

    /**
     * [Greek][Language.EL], [Cyprus][Market.CY]
     */
    EL_CY(Language.EL, Market.CY),

    /**
     * [Greek][Language.EL], [Greece][Market.GR]
     */
    EL_GR(Language.EL, Market.GR),
    /**
     * [English][Language.EN]
     */

    /**
     * [English][Language.EN], [Australia][Market.AU]
     */
    EN_AU(Language.EN, Market.AU),

    /**
     * [English][Language.EN], [Canada][Market.CA]
     */
    EN_CA(Language.EN, Market.CA),

    /**
     * [English][Language.EN], [United Kingdom][Market.GB]
     */
    EN_GB(Language.EN, Market.GB),

    /**
     * [English][Language.EN], [Hong Kong][Market.HK]
     *
     * @since 1.22
     */
    EN_HK(Language.EN, Market.HK),

    /**
     * [English][Language.EN], [Ireland][Market.IE]
     */
    EN_IE(Language.EN, Market.IE),

    /**
     * [English][Language.EN], [India][Market.IN]
     */
    EN_IN(Language.EN, Market.IN),

    /**
     * [English][Language.EN], [Malta][Market.MT]
     */
    EN_MT(Language.EN, Market.MT),

    /**
     * [English][Language.EN], [New Zealand][Market.NZ]
     */
    EN_NZ(Language.EN, Market.NZ),

    /**
     * [English][Language.EN], [Philippines][Market.PH]
     */
    EN_PH(Language.EN, Market.PH),

    /**
     * [English][Language.EN], [Singapore][Market.SG]
     */
    EN_SG(Language.EN, Market.SG),

    /**
     * [English][Language.EN], [United States][Market.US]
     */
    EN_US(Language.EN, Market.US),

    /**
     * [English][Language.EN], [South Africa][Market.ZA]
     */
    EN_ZA(Language.EN, Market.ZA),
    /**
     * [Spanish][Language.ES]
     */

    /**
     * [Spanish][Language.ES], [Argentina][Market.AR]
     */
    ES_AR(Language.ES, Market.AR),

    /**
     * [Spanish][Language.ES], [Bolivia, Plurinational State of][Market.BO]
     */
    ES_BO(Language.ES, Market.BO),

    /**
     * [Spanish][Language.ES], [Chile][Market.CL]
     */
    ES_CL(Language.ES, Market.CL),

    /**
     * [Spanish][Language.ES], [Colombia][Market.CO]
     */
    ES_CO(Language.ES, Market.CO),

    /**
     * [Spanish][Language.ES], [Costa Rica][Market.CR]
     */
    ES_CR(Language.ES, Market.CR),

    /**
     * [Spanish][Language.ES], [Dominican Republic][Market.DO]
     */
    ES_DO(Language.ES, Market.DO),

    /**
     * [Spanish][Language.ES], [Ecuador][Market.EC]
     */
    ES_EC(Language.ES, Market.EC),

    /**
     * [Spanish][Language.ES], [Spain][Market.ES]
     */
    ES_ES(Language.ES, Market.ES),

    /**
     * [Spanish][Language.ES], [Guatemala][Market.GT]
     */
    ES_GT(Language.ES, Market.GT),

    /**
     * [Spanish][Language.ES], [Honduras][Market.HN]
     */
    ES_HN(Language.ES, Market.HN),

    /**
     * [Spanish][Language.ES], [Mexico][Market.MX]
     */
    ES_MX(Language.ES, Market.MX),

    /**
     * [Spanish][Language.ES], [Nicaragua][Market.NI]
     */
    ES_NI(Language.ES, Market.NI),

    /**
     * [Spanish][Language.ES], [Panama][Market.PA]
     */
    ES_PA(Language.ES, Market.PA),

    /**
     * [Spanish][Language.ES], [Peru][Market.PE]
     */
    ES_PE(Language.ES, Market.PE),

    /**
     * [Spanish][Language.ES], [Puerto Rico][Market.PR]
     */
    ES_PR(Language.ES, Market.PR),

    /**
     * [Spanish][Language.ES], [Paraguay][Market.PY]
     */
    ES_PY(Language.ES, Market.PY),

    /**
     * [Spanish][Language.ES], [El Salvador][Market.SV]
     */
    ES_SV(Language.ES, Market.SV),

    /**
     * [Spanish][Language.ES], [United States][Market.US]
     */
    ES_US(Language.ES, Market.US),

    /**
     * [Spanish][Language.ES], [Uruguay][Market.UY]
     */
    ES_UY(Language.ES, Market.UY),

    /**
     * [Spanish][Language.ES], [Venezuela, Bolivarian Republic of][Market.VE]
     */
    ES_VE(Language.ES, Market.VE),
    /**
     * [Estonian][Language.ET]
     */

    /**
     * [Estonian][Language.ET], [Estonia][Market.EE]
     */
    ET_EE(Language.ET, Market.EE),
    /**
     * [Farsi][Language.FA]
     *
     * @since 1.21
     */

    /**
     * [Farsi][Language.FA], [Iran][Market.IR]
     *
     * @since 1.21
     */
    FA_IR(Language.FA, Market.IR),
    /**
     * [Finnish][Language.FI]
     */

    /**
     * [Finnish][Language.FI], [Finland][Market.FI]
     */
    FI_FI(Language.FI, Market.FI),
    /**
     * [French][Language.FR]
     */

    /**
     * [French][Language.FR], [Belgium][Market.BE]
     */
    FR_BE(Language.FR, Market.BE),

    /**
     * [French][Language.FR], [Canada][Market.CA]
     */
    FR_CA(Language.FR, Market.CA),

    /**
     * [French][Language.FR], [Switzerland][Market.CH]
     */
    FR_CH(Language.FR, Market.CH),

    /**
     * [French][Language.FR], [France][Market.FR]
     */
    FR_FR(Language.FR, Market.FR),

    /**
     * [French][Language.FR], [Luxembourg][Market.LU]
     */
    FR_LU(Language.FR, Market.LU),
    /**
     * [Irish][Language.GA]
     */

    /**
     * [Irish][Language.GA], [Ireland][Market.IE]
     */
    GA_IE(Language.GA, Market.IE),
    /**
     * [Hebrew][Language.HE]
     */

    /**
     * [Hebrew][Language.HE], [Israel][Market.IL]
     */
    HE_IL(Language.HE, Market.IL),

    /**
     * [Hindi][Language.HI], [India][Market.IN]
     */
    HI_IN(Language.HI, Market.IN),
    /**
     * [Croatian][Language.HR]
     */

    /**
     * [Croatian][Language.HR], [Croatia][Market.HR]
     */
    HR_HR(Language.HR, Market.HR),
    /**
     * [Hungarian][Language.HU]
     */

    /**
     * [Hungarian][Language.HU], [Hungary][Market.HU]
     */
    HU_HU(Language.HU, Market.HU),
    /**
     * [Indonesian][Language.ID]
     */

    /**
     * [Indonesian][Language.ID], [Indonesia][Market.ID]
     */
    ID_ID(Language.ID, Market.ID),
    /**
     * [Icelandic][LanguageCode. is]
     */

    /**
     * [Icelandic][LanguageCode. is], [Iceland][Market.IS]
     */
    IS_IS(Language.IS, Market.IS),
    /**
     * [Italian][Language.IT]
     */

    /**
     * [Italian][Language.IT], [Switzerland][Market.CH]
     */
    IT_CH(Language.IT, Market.CH),

    /**
     * [Italian][Language.IT], [Italy][Market.IT]
     */
    IT_IT(Language.IT, Market.IT),
    /**
     * [Japanese][Language.JA]
     */

    /**
     * [Japanese][Language.JA], [Japan][Market.JP]
     */
    JA_JP(Language.JA, Market.JP),

    /**
     * [Kazakh][Language.KK], [Kazakhstan][Market.KZ]
     *
     * @since 1.22
     */
    KK_KZ(Language.KK, Market.KZ),
    /**
     * [Korean][Language.KO]
     */

    /**
     * [Korean][Language.KO], [Korea, Republic of][Market.KR]
     */
    KO_KR(Language.KO, Market.KR),
    /**
     * [Lithuanian][Language.LT]
     */

    /**
     * [Lithuanian][Language.LT], [Lithuania][Market.LT]
     */
    LT_LT(Language.LT, Market.LT),
    /**
     * [Latvian][Language.LV]
     */

    /**
     * [Latvian][Language.LV], [Latvia][Market.LV]
     */
    LV_LV(Language.LV, Market.LV),
    /**
     * [Macedonian][Language.MK]
     */

    /**
     * [Macedonian][Language.MK], [Macedonia, the former Yugoslav Republic of][Market.MK]
     */
    MK_MK(Language.MK, Market.MK),
    /**
     * [Malay][Language.MS]
     */

    /**
     * [Malay][Language.MS], [Malaysia][Market.MY]
     */
    MS_MY(Language.MS, Market.MY),
    /**
     * [Maltese][Language.MT]
     */

    /**
     * [Maltese][Language.MT], [Malta][Market.MT]
     */
    MT_MT(Language.MT, Market.MT),
    /**
     * [Norwegian Bokm&amp;aring;l][Language.NB]
     *
     * @since 1.8
     */

    /**
     * [Norwegian Bokm&amp;aring;l][Language.NB], [Norway][Market.NO]
     *
     * @since 1.8
     */
    NB_NO(Language.NB, Market.NO),
    /**
     * [Dutch][Language.NL]
     */

    /**
     * [Dutch][Language.NL], [Belgium][Market.BE]
     */
    NL_BE(Language.NL, Market.BE),

    /**
     * [Dutch][Language.NL], [Netherlands][Market.NL]
     */
    NL_NL(Language.NL, Market.NL),

    /**
     * [Norwegian Nynorsk][Language.NN], [Norway][Market.NO]
     */
    NN_NO(Language.NN, Market.NO),
    /**
     * [Norwegian][Language.NO]
     *
     * @see [Sprkkoder for POSIX locale i Norge](http://i18n.skolelinux.no/localekoder.txt)
     *
     * @see [Red Hat Bugzilla – Bug 532487 Legacy Norwegian locale
     ](https://bugzilla.redhat.com/show_bug.cgi?id=532487) */

    /**
     * [Norwegian][Language.NO], [Norway][Market.NO]
     *
     * @see [Sprkkoder for POSIX locale i Norge](http://i18n.skolelinux.no/localekoder.txt)
     *
     * @see [Red Hat Bugzilla – Bug 532487 Legacy Norwegian locale
     ](https://bugzilla.redhat.com/show_bug.cgi?id=532487) */
    NO_NO(Language.NO, Market.NO),
    /**
     * [Polish][Language.PL]
     */

    /**
     * [Polish][Language.PL], [Poland][Market.PL]
     */
    PL_PL(Language.PL, Market.PL),
    /**
     * [Portuguese][Language.PT]
     */

    /**
     * [Portuguese][Language.PT], [Brazil][Market.BR]
     */
    PT_BR(Language.PT, Market.BR),

    /**
     * [Portuguese][Language.PT], [Portugal][Market.PT]
     */
    PT_PT(Language.PT, Market.PT),
    /**
     * [Romanian][Language.RO]
     */

    /**
     * [Romanian][Language.RO], [Moldova, Republic of][Market.MD]
     */
    RO_MD(Language.RO, Market.MD),

    /**
     * [Romanian][Language.RO], [Romania][Market.RO]
     */
    RO_RO(Language.RO, Market.RO),
    /**
     * [Russian][Language.RU]
     */

    /**
     * [Russian][Language.RU], [Kazakhstan][Market.KZ]
     *
     * @since 1.22
     */
    RU_KZ(Language.RU, Market.KZ),

    /**
     * [Russian][Language.RU], [Russian Federation][Market.RU]
     */
    RU_RU(Language.RU, Market.RU),
    /**
     * [Northern Sami][Language.SE]
     *
     * @since 1.8
     */

    /**
     * [Northern Sami][Language.SE], [Norway][Market.NO]
     *
     * @since 1.8
     */
    SE_NO(Language.SE, Market.NO),
    /**
     * [Slovak][Language.SK]
     */

    /**
     * [Slovak][Language.SK], [Slovakia][Market.SK]
     */
    SK_SK(Language.SK, Market.SK),
    /**
     * [Slovene][Language.SL]
     */

    /**
     * [Slovene][Language.SL], [Slovenia][Market.SI]
     */
    SL_SI(Language.SL, Market.SI),
    /**
     * [Albanian][Language.SQ]
     */

    /**
     * [Albanian][Language.SQ], [Albania][Market.AL]
     */
    SQ_AL(Language.SQ, Market.AL),
    /**
     * [Serbian][Language.SR]
     */

    /**
     * [Serbian][Language.SR], [Bosnia and Herzegovina][Market.BA]
     */
    SR_BA(Language.SR, Market.BA),

    /**
     * [Serbian][Language.SR], [Serbia and Montenegro][Market.CS]
     */
    SR_CS(Language.SR, Market.CS),

    /**
     * [Serbian][Language.SR], [Montenegro][Market.ME]
     */
    SR_ME(Language.SR, Market.ME),

    /**
     * [Serbian][Language.SR], [Serbia][Market.RS]
     */
    SR_RS(Language.SR, Market.RS),
    /**
     * [Swedish][Language.SV]
     */

    /**
     * [Swedish][Language.SV], [Sweden][Market.SE]
     */
    SV_SE(Language.SV, Market.SE),
    /**
     * [Thai][Language.TH]
     */

    /**
     * [Thai][Language.TH], [Thailand][Market.TH]
     */
    TH_TH(Language.TH, Market.TH),
    /**
     * [Turkish][Language.TR]
     */

    /**
     * [Turkish][Language.TR], [Turkey][Market.TR]
     */
    TR_TR(Language.TR, Market.TR),
    /**
     * [Ukrainian][Language.UK]
     */

    /**
     * [Ukrainian][Language.UK], [Ukraine][Market.UA]
     */
    UK_UA(Language.UK, Market.UA),
    /**
     * [Vietnamese][Language.VI]
     */
    /**
     * [Vietnamese][Language.VI], [Viet Nam][Market.VN]
     */
    VI_VN(Language.VI, Market.VN),

    /**
     * [Chinese][Language.ZH], [China][Market.CN]
     */
    ZH_CN(Language.ZH, Market.CN),

    /**
     * [Chinese][Language.ZH], [Hong Kong][Market.HK]
     */
    ZH_HK(Language.ZH, Market.HK),

    /**
     * [Chinese][Language.ZH], [Singapore][Market.SG]
     */
    ZH_SG(Language.ZH, Market.SG),

    /**
     * [Chinese][Language.ZH], [Taiwan, Province of China][Market.TW]
     */
    ZH_TW(Language.ZH, Market.TW);

    /**
     * Get the string representation of this locale code. Its format is
     * either of the following:
     *
     *
     *  * *language*
     *  * *language*`-`*country*
     *
     *
     *
     *
     * where *language* is an [ISO 639-1](http://en.wikipedia.org/wiki/ISO_639-1) code
     * and *country* is an [ISO 3166-1
 * alpha-2](http://en.wikipedia.org/wiki/ISO_3166-1_alpha-2) code.
     *
     *
     * @return
     * The string representation of this locale code.
     */
    override fun toString(): String {
        return name
    }

    public companion object {
        public fun from(language: Language, country: Market): Locale? {
            return entries.find { locale -> locale.language == language && locale.country == country }
        }
    }
}
