/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
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
 * String language = code.[.getLanguage].[getName()][Language.getName];
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
enum class Locale(val language: Language, val country: Market) {
    /**
     * [Arabic][Language.ar]
     */

    /**
     * [Arabic][Language.ar], [United Arab Emirates][Market.AE]
     */
    ar_AE(Language.ar, Market.AE),
    /**
     * [Arabic][Language.ar], [Bahrain][Market.BH]
     */
    ar_BH(Language.ar, Market.BH),
    /**
     * [Arabic][Language.ar], [Algeria][Market.DZ]
     */
    ar_DZ(Language.ar, Market.DZ),
    /**
     * [Arabic][Language.ar], [Egypt][Market.EG]
     */
    ar_EG(Language.ar, Market.EG),
    /**
     * [Arabic][Language.ar], [Iraq][Market.IQ]
     */
    ar_IQ(Language.ar, Market.IQ),
    /**
     * [Arabic][Language.ar], [Jordan][Market.JO]
     */
    ar_JO(Language.ar, Market.JO),
    /**
     * [Arabic][Language.ar], [Kuwait][Market.KW]
     */
    ar_KW(Language.ar, Market.KW),
    /**
     * [Arabic][Language.ar], [Lebanon][Market.LB]
     */
    ar_LB(Language.ar, Market.LB),
    /**
     * [Arabic][Language.ar], [Libya][Market.LY]
     */
    ar_LY(Language.ar, Market.LY),
    /**
     * [Arabic][Language.ar], [Morocco][Market.MA]
     */
    ar_MA(Language.ar, Market.MA),
    /**
     * [Arabic][Language.ar], [Oman][Market.OM]
     */
    ar_OM(Language.ar, Market.OM),
    /**
     * [Arabic][Language.ar], [Qatar][Market.QA]
     */
    ar_QA(Language.ar, Market.QA),
    /**
     * [Arabic][Language.ar], [Saudi Arabia][Market.SA]
     */
    ar_SA(Language.ar, Market.SA),
    /**
     * [Arabic][Language.ar], [Sudan][Market.SD]
     */
    ar_SD(Language.ar, Market.SD),
    /**
     * [Arabic][Language.ar], [Syrian Arab Republic][Market.SY]
     */
    ar_SY(Language.ar, Market.SY),
    /**
     * [Arabic][Language.ar], [Tunisia][Market.TN]
     */
    ar_TN(Language.ar, Market.TN),
    /**
     * [Arabic][Language.ar], [Yemen][Market.YE]
     */
    ar_YE(Language.ar, Market.YE),
    /**
     * [Belarusian][Language.be]
     */

    /**
     * [Belarusian][Language.be], [Belarus][Market.BY]
     */
    be_BY(Language.be, Market.BY),
    /**
     * [Bulgarian][Language.bg]
     */

    /**
     * [Bulgarian][Language.bg], [Bulgaria][Market.BG]
     */
    bg_BG(Language.bg, Market.BG),
    /**
     * [Catalan][Language.ca]
     */

    /**
     * [Catalan][Language.ca], [Spain][Market.ES]
     */
    ca_ES(Language.ca, Market.ES),
    /**
     * [Czech][Language.cs]
     */

    /**
     * [Czech][Language.cs], [Czech Republic][Market.CZ]
     */
    cs_CZ(Language.cs, Market.CZ),
    /**
     * [Danish][Language.da]
     */

    /**
     * [Danish][Language.da], [Denmark][Market.DK]
     */
    da_DK(Language.da, Market.DK),
    /**
     * [German][Language.de]
     */

    /**
     * [German][Language.de], [Austria][Market.AT]
     */
    de_AT(Language.de, Market.AT),
    /**
     * [German][Language.de], [Switzerland][Market.CH]
     */
    de_CH(Language.de, Market.CH),
    /**
     * [German][Language.de], [Germany][Market.DE]
     */
    de_DE(Language.de, Market.DE),
    /**
     * [German][Language.de], [Luxembourg][Market.LU]
     */
    de_LU(Language.de, Market.LU),
    /**
     * [Greek][Language.el]
     */

    /**
     * [Greek][Language.el], [Cyprus][Market.CY]
     */
    el_CY(Language.el, Market.CY),
    /**
     * [Greek][Language.el], [Greece][Market.GR]
     */
    el_GR(Language.el, Market.GR),
    /**
     * [English][Language.en]
     */

    /**
     * [English][Language.en], [Australia][Market.AU]
     */
    en_AU(Language.en, Market.AU),
    /**
     * [English][Language.en], [Canada][Market.CA]
     */
    en_CA(Language.en, Market.CA),
    /**
     * [English][Language.en], [United Kingdom][Market.GB]
     */
    en_GB(Language.en, Market.GB),
    /**
     * [English][Language.en], [Hong Kong][Market.HK]
     *
     * @since 1.22
     */
    en_HK(Language.en, Market.HK),
    /**
     * [English][Language.en], [Ireland][Market.IE]
     */
    en_IE(Language.en, Market.IE),
    /**
     * [English][Language.en], [India][Market.IN]
     */
    en_IN(Language.en, Market.IN),
    /**
     * [English][Language.en], [Malta][Market.MT]
     */
    en_MT(Language.en, Market.MT),
    /**
     * [English][Language.en], [New Zealand][Market.NZ]
     */
    en_NZ(Language.en, Market.NZ),
    /**
     * [English][Language.en], [Philippines][Market.PH]
     */
    en_PH(Language.en, Market.PH),
    /**
     * [English][Language.en], [Singapore][Market.SG]
     */
    en_SG(Language.en, Market.SG),
    /**
     * [English][Language.en], [United States][Market.US]
     */
    en_US(Language.en, Market.US),
    /**
     * [English][Language.en], [South Africa][Market.ZA]
     */
    en_ZA(Language.en, Market.ZA),
    /**
     * [Spanish][Language.es]
     */

    /**
     * [Spanish][Language.es], [Argentina][Market.AR]
     */
    es_AR(Language.es, Market.AR),
    /**
     * [Spanish][Language.es], [Bolivia, Plurinational State of][Market.BO]
     */
    es_BO(Language.es, Market.BO),
    /**
     * [Spanish][Language.es], [Chile][Market.CL]
     */
    es_CL(Language.es, Market.CL),
    /**
     * [Spanish][Language.es], [Colombia][Market.CO]
     */
    es_CO(Language.es, Market.CO),
    /**
     * [Spanish][Language.es], [Costa Rica][Market.CR]
     */
    es_CR(Language.es, Market.CR),
    /**
     * [Spanish][Language.es], [Dominican Republic][Market.DO]
     */
    es_DO(Language.es, Market.DO),
    /**
     * [Spanish][Language.es], [Ecuador][Market.EC]
     */
    es_EC(Language.es, Market.EC),
    /**
     * [Spanish][Language.es], [Spain][Market.ES]
     */
    es_ES(Language.es, Market.ES),
    /**
     * [Spanish][Language.es], [Guatemala][Market.GT]
     */
    es_GT(Language.es, Market.GT),
    /**
     * [Spanish][Language.es], [Honduras][Market.HN]
     */
    es_HN(Language.es, Market.HN),
    /**
     * [Spanish][Language.es], [Mexico][Market.MX]
     */
    es_MX(Language.es, Market.MX),
    /**
     * [Spanish][Language.es], [Nicaragua][Market.NI]
     */
    es_NI(Language.es, Market.NI),
    /**
     * [Spanish][Language.es], [Panama][Market.PA]
     */
    es_PA(Language.es, Market.PA),
    /**
     * [Spanish][Language.es], [Peru][Market.PE]
     */
    es_PE(Language.es, Market.PE),
    /**
     * [Spanish][Language.es], [Puerto Rico][Market.PR]
     */
    es_PR(Language.es, Market.PR),
    /**
     * [Spanish][Language.es], [Paraguay][Market.PY]
     */
    es_PY(Language.es, Market.PY),
    /**
     * [Spanish][Language.es], [El Salvador][Market.SV]
     */
    es_SV(Language.es, Market.SV),
    /**
     * [Spanish][Language.es], [United States][Market.US]
     */
    es_US(Language.es, Market.US),
    /**
     * [Spanish][Language.es], [Uruguay][Market.UY]
     */
    es_UY(Language.es, Market.UY),
    /**
     * [Spanish][Language.es], [Venezuela, Bolivarian Republic of][Market.VE]
     */
    es_VE(Language.es, Market.VE),
    /**
     * [Estonian][Language.et]
     */

    /**
     * [Estonian][Language.et], [Estonia][Market.EE]
     */
    et_EE(Language.et, Market.EE),
    /**
     * [Farsi][Language.fa]
     *
     * @since 1.21
     */

    /**
     * [Farsi][Language.fa], [Iran][Market.IR]
     *
     * @since 1.21
     */
    fa_IR(Language.fa, Market.IR),
    /**
     * [Finnish][Language.fi]
     */

    /**
     * [Finnish][Language.fi], [Finland][Market.FI]
     */
    fi_FI(Language.fi, Market.FI),
    /**
     * [French][Language.fr]
     */

    /**
     * [French][Language.fr], [Belgium][Market.BE]
     */
    fr_BE(Language.fr, Market.BE),
    /**
     * [French][Language.fr], [Canada][Market.CA]
     */
    fr_CA(Language.fr, Market.CA),
    /**
     * [French][Language.fr], [Switzerland][Market.CH]
     */
    fr_CH(Language.fr, Market.CH),
    /**
     * [French][Language.fr], [France][Market.FR]
     */
    fr_FR(Language.fr, Market.FR),
    /**
     * [French][Language.fr], [Luxembourg][Market.LU]
     */
    fr_LU(Language.fr, Market.LU),
    /**
     * [Irish][Language.ga]
     */

    /**
     * [Irish][Language.ga], [Ireland][Market.IE]
     */
    ga_IE(Language.ga, Market.IE),
    /**
     * [Hebrew][Language.he]
     */

    /**
     * [Hebrew][Language.he], [Israel][Market.IL]
     */
    he_IL(Language.he, Market.IL),
    /**
     * [Hindi][Language.hi], [India][Market.IN]
     */
    hi_IN(Language.hi, Market.IN),
    /**
     * [Croatian][Language.hr]
     */

    /**
     * [Croatian][Language.hr], [Croatia][Market.HR]
     */
    hr_HR(Language.hr, Market.HR),
    /**
     * [Hungarian][Language.hu]
     */

    /**
     * [Hungarian][Language.hu], [Hungary][Market.HU]
     */
    hu_HU(Language.hu, Market.HU),
    /**
     * [Indonesian][Language.id]
     */

    /**
     * [Indonesian][Language.id], [Indonesia][Market.ID]
     */
    id_ID(Language.id, Market.ID),
    /**
     * [Icelandic][LanguageCode. is]
     */

    /**
     * [Icelandic][LanguageCode. is], [Iceland][Market.IS]
     */
    is_IS(Language.`is`, Market.IS),
    /**
     * [Italian][Language.it]
     */

    /**
     * [Italian][Language.it], [Switzerland][Market.CH]
     */
    it_CH(Language.it, Market.CH),
    /**
     * [Italian][Language.it], [Italy][Market.IT]
     */
    it_IT(Language.it, Market.IT),
    /**
     * [Japanese][Language.ja]
     */

    /**
     * [Japanese][Language.ja], [Japan][Market.JP]
     */
    ja_JP(Language.ja, Market.JP),
    /**
     * [Kazakh][Language.kk], [Kazakhstan][Market.KZ]
     *
     * @since 1.22
     */
    kk_KZ(Language.kk, Market.KZ),
    /**
     * [Korean][Language.ko]
     */

    /**
     * [Korean][Language.ko], [Korea, Republic of][Market.KR]
     */
    ko_KR(Language.ko, Market.KR),
    /**
     * [Lithuanian][Language.lt]
     */

    /**
     * [Lithuanian][Language.lt], [Lithuania][Market.LT]
     */
    lt_LT(Language.lt, Market.LT),
    /**
     * [Latvian][Language.lv]
     */

    /**
     * [Latvian][Language.lv], [Latvia][Market.LV]
     */
    lv_LV(Language.lv, Market.LV),
    /**
     * [Macedonian][Language.mk]
     */

    /**
     * [Macedonian][Language.mk], [Macedonia, the former Yugoslav Republic of][Market.MK]
     */
    mk_MK(Language.mk, Market.MK),
    /**
     * [Malay][Language.ms]
     */

    /**
     * [Malay][Language.ms], [Malaysia][Market.MY]
     */
    ms_MY(Language.ms, Market.MY),
    /**
     * [Maltese][Language.mt]
     */

    /**
     * [Maltese][Language.mt], [Malta][Market.MT]
     */
    mt_MT(Language.mt, Market.MT),
    /**
     * [Norwegian Bokm&amp;aring;l][Language.nb]
     *
     * @since 1.8
     */

    /**
     * [Norwegian Bokm&amp;aring;l][Language.nb], [Norway][Market.NO]
     *
     * @since 1.8
     */
    nb_NO(Language.nb, Market.NO),
    /**
     * [Dutch][Language.nl]
     */

    /**
     * [Dutch][Language.nl], [Belgium][Market.BE]
     */
    nl_BE(Language.nl, Market.BE),
    /**
     * [Dutch][Language.nl], [Netherlands][Market.NL]
     */
    nl_NL(Language.nl, Market.NL),
    /**
     * [Norwegian Nynorsk][Language.nn], [Norway][Market.NO]
     */
    nn_NO(Language.nn, Market.NO),
    /**
     * [Norwegian][Language.no]
     *
     * @see [Sprkkoder for POSIX locale i Norge](http://i18n.skolelinux.no/localekoder.txt)
     *
     * @see [Red Hat Bugzilla – Bug 532487 Legacy Norwegian locale
    ](https://bugzilla.redhat.com/show_bug.cgi?id=532487) */

    /**
     * [Norwegian][Language.no], [Norway][Market.NO]
     *
     * @see [Sprkkoder for POSIX locale i Norge](http://i18n.skolelinux.no/localekoder.txt)
     *
     * @see [Red Hat Bugzilla – Bug 532487 Legacy Norwegian locale
    ](https://bugzilla.redhat.com/show_bug.cgi?id=532487) */
    no_NO(Language.no, Market.NO),
    /**
     * [Polish][Language.pl]
     */

    /**
     * [Polish][Language.pl], [Poland][Market.PL]
     */
    pl_PL(Language.pl, Market.PL),
    /**
     * [Portuguese][Language.pt]
     */

    /**
     * [Portuguese][Language.pt], [Brazil][Market.BR]
     */
    pt_BR(Language.pt, Market.BR),
    /**
     * [Portuguese][Language.pt], [Portugal][Market.PT]
     */
    pt_PT(Language.pt, Market.PT),
    /**
     * [Romanian][Language.ro]
     */

    /**
     * [Romanian][Language.ro], [Moldova, Republic of][Market.MD]
     */
    ro_MD(Language.ro, Market.MD),
    /**
     * [Romanian][Language.ro], [Romania][Market.RO]
     */
    ro_RO(Language.ro, Market.RO),
    /**
     * [Russian][Language.ru]
     */

    /**
     * [Russian][Language.ru], [Kazakhstan][Market.KZ]
     *
     * @since 1.22
     */
    ru_KZ(Language.ru, Market.KZ),
    /**
     * [Russian][Language.ru], [Russian Federation][Market.RU]
     */
    ru_RU(Language.ru, Market.RU),
    /**
     * [Northern Sami][Language.se]
     *
     * @since 1.8
     */

    /**
     * [Northern Sami][Language.se], [Norway][Market.NO]
     *
     * @since 1.8
     */
    se_NO(Language.se, Market.NO),
    /**
     * [Slovak][Language.sk]
     */

    /**
     * [Slovak][Language.sk], [Slovakia][Market.SK]
     */
    sk_SK(Language.sk, Market.SK),
    /**
     * [Slovene][Language.sl]
     */

    /**
     * [Slovene][Language.sl], [Slovenia][Market.SI]
     */
    sl_SI(Language.sl, Market.SI),
    /**
     * [Albanian][Language.sq]
     */

    /**
     * [Albanian][Language.sq], [Albania][Market.AL]
     */
    sq_AL(Language.sq, Market.AL),
    /**
     * [Serbian][Language.sr]
     */

    /**
     * [Serbian][Language.sr], [Bosnia and Herzegovina][Market.BA]
     */
    sr_BA(Language.sr, Market.BA),
    /**
     * [Serbian][Language.sr], [Serbia and Montenegro][Market.CS]
     */
    sr_CS(Language.sr, Market.CS),
    /**
     * [Serbian][Language.sr], [Montenegro][Market.ME]
     */
    sr_ME(Language.sr, Market.ME),
    /**
     * [Serbian][Language.sr], [Serbia][Market.RS]
     */
    sr_RS(Language.sr, Market.RS),
    /**
     * [Swedish][Language.sv]
     */

    /**
     * [Swedish][Language.sv], [Sweden][Market.SE]
     */
    sv_SE(Language.sv, Market.SE),
    /**
     * [Thai][Language.th]
     */

    /**
     * [Thai][Language.th], [Thailand][Market.TH]
     */
    th_TH(Language.th, Market.TH),
    /**
     * [Turkish][Language.tr]
     */

    /**
     * [Turkish][Language.tr], [Turkey][Market.TR]
     */
    tr_TR(Language.tr, Market.TR),
    /**
     * [Ukrainian][Language.uk]
     */

    /**
     * [Ukrainian][Language.uk], [Ukraine][Market.UA]
     */
    uk_UA(Language.uk, Market.UA),
    /**
     * [Vietnamese][Language.vi]
     */
    /**
     * [Vietnamese][Language.vi], [Viet Nam][Market.VN]
     */
    vi_VN(Language.vi, Market.VN),

    /**
     * [Chinese][Language.zh], [China][Market.CN]
     */
    zh_CN(Language.zh, Market.CN),
    /**
     * [Chinese][Language.zh], [Hong Kong][Market.HK]
     */
    zh_HK(Language.zh, Market.HK),
    /**
     * [Chinese][Language.zh], [Singapore][Market.SG]
     */
    zh_SG(Language.zh, Market.SG),
    /**
     * [Chinese][Language.zh], [Taiwan, Province of China][Market.TW]
     */
    zh_TW(Language.zh, Market.TW);

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

    companion object {
        fun from(language: Language, country: Market): Locale? {
            return values().find { locale -> locale.language == language && locale.country == country }
        }
    }
}
