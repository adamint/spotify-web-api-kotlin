/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2022; Original author: Adam Ratzman */
package com.adamratzman.spotify.utils

import kotlinx.serialization.Serializable

/**
 * [ISO 639-1](http://en.wikipedia.org/wiki/ISO_639-1)
 * language code.
 *
 *
 *
 * Enum names of this enum themselves are represented by
 * [ISO 639-1](http://en.wikipedia.org/wiki/ISO_639-1) code
 * (2-letter lower-case alphabets).
 *
 *
 * <pre style="background-color: #EEEEEE; margin-left: 2em; margin-right: 2em; border: 1px solid black; padding: 0.5em;">
 * <span style="color: darkgreen;">// List all the language codes.</span>
 * for (LanguageCode code : LanguageCode.values())
 * {
 * <span style="color: darkgreen;">// For examp LE, "[ar] Arabic" is printed.</span>
 * System.out.format(<span style="color: darkred;">"[%s] %s\n"</spa N>, co DE, code.[.getName]);
 * }
 *
 * <span style="color: darkgreen;">// Get a LanguageCode instance by ISO 639-1 code.</span>
 * LanguageCode code = LanguageCode.[getByCode][.getByCode](<span style="color: darkred;">"fr"</span>);
 *
 * <span style="color: darkgreen;">// Convert to a Locale instance.</span>
 * Locale locale = code.[.toLocale];
 *
 * <span style="color: darkgreen;">// Get a LanguageCode by a Locale instance.</span>
 * code = LanguageCode.[getByLocale][.getByLocale](locale);
 *
 * <span style="color: darkgreen;">// Get a list by a regular expression for names.
 * //
 * // The list will contain:
 * //
 * //     LanguageCode.an : Aragonese
 * //     LanguageCode.ja : Japanese
 * //     LanguageCode.jv : Javanese
 * //     LanguageCode.su : Sundanese
 * //     LanguageCode.zh : Chinese
 * //</span>
 * List&lt;LanguageCode&gt; list = LanguageCode.[findByName][.findByName](<span style="color: darkred;">".*nese"</span>);
</pre> *
 *
 * @author Takahiko Kawasaki
 */
@Serializable
public enum class Language {
    /**
     * [Afar](http://en.wikipedia.org/wiki/Afar_language)
     * ([aar][LanguageAlpha3Code.aar]).
     */
    AA,

    /**
     * [Abkhaz](http://en.wikipedia.org/wiki/Abkhaz_language)
     * ([abk][LanguageAlpha3Code.abk]).
     */
    AB,

    /**
     * [Avestan](http://en.wikipedia.org/wiki/Avestan_language)
     * ([ave][LanguageAlpha3Code.ave]).
     */
    AE,

    /**
     * [Afrikaans](http://en.wikipedia.org/wiki/Afrikaans_language)
     * ([afr][LanguageAlpha3Code.afr]).
     */
    AF,

    /**
     * [Akan](http://en.wikipedia.org/wiki/Akan_language)
     * ([aka][LanguageAlpha3Code.aka]).
     */
    AK,

    /**
     * [Amharic](http://en.wikipedia.org/wiki/Amharic_language)
     * ([amh][LanguageAlpha3Code.amh]).
     */
    AM,

    /**
     * [Aragonese](http://en.wikipedia.org/wiki/Aragonese_language)
     * ([arg][LanguageAlpha3Code.arg]).
     */
    AN,

    /**
     * [Arabic](http://en.wikipedia.org/wiki/Arabic_language)
     * ([ara][LanguageAlpha3Code.ara]).
     */
    AR,

    /**
     * [Assamese](http://en.wikipedia.org/wiki/Assamese_language)
     * ([asm][LanguageAlpha3Code.asm]).
     */
    AS,

    /**
     * [Avaric](http://en.wikipedia.org/wiki/Avar_language)
     * ([ava][LanguageAlpha3Code.ava]).
     */
    AV,

    /**
     * [Aymara](http://en.wikipedia.org/wiki/Aymara_language)
     * ([aym][LanguageAlpha3Code.aym]).
     */
    AY,

    /**
     * [Azerbaijani](http://en.wikipedia.org/wiki/Azerbaijani_language)
     * ([aze][LanguageAlpha3Code.aze]).
     */
    AZ,

    /**
     * [Bashkir](http://en.wikipedia.org/wiki/Bashkir_language)
     * ([bak][LanguageAlpha3Code.bak]).
     */
    BA,

    /**
     * [Belarusian](http://en.wikipedia.org/wiki/Belarusian_language)
     * ([bel][LanguageAlpha3Code.bel]).
     */
    BE,

    /**
     * [Bulgarian](http://en.wikipedia.org/wiki/Bulgarian_language)
     * ([bul][LanguageAlpha3Code.bul]).
     */
    BG,

    /**
     * [Bihari](http://en.wikipedia.org/wiki/Bihari_languages)
     * ([bih][LanguageAlpha3Code.bih]).
     */
    BH,

    /**
     * [Bislama](http://en.wikipedia.org/wiki/Bislama_language)
     * ([bis][LanguageAlpha3Code.bis]).
     */
    BI,

    /**
     * [Bambara](http://en.wikipedia.org/wiki/Bambara_language)
     * ([bam][LanguageAlpha3Code.bam]).
     */
    BM,

    /**
     * [Bengali](http://en.wikipedia.org/wiki/Bengali_language)
     * ([ben][LanguageAlpha3Code.ben]).
     */
    BN,

    /**
     * [Tibetan](http://en.wikipedia.org/wiki/Standard_Tibetan)
     * ([bod][LanguageAlpha3Code.bo D], [tib][LanguageAlpha3Code.tib]).
     */
    BO,

    /**
     * [Breton](http://en.wikipedia.org/wiki/Breton_language)
     * ([bre][LanguageAlpha3Code.bre]).
     */
    BR,

    /**
     * [Bosnian](http://en.wikipedia.org/wiki/Bosnian_language)
     * ([bos][LanguageAlpha3Code.bos]).
     */
    BS,

    /**
     * [Catalan](http://en.wikipedia.org/wiki/Catalan_language)
     * ([cat][LanguageAlpha3Code.cat]).
     */
    CA,

    /**
     * [Chechen](http://en.wikipedia.org/wiki/Chechen_language)
     * ([che][LanguageAlpha3Code.che]).
     */
    CE,

    /**
     * [Chamorro](http://en.wikipedia.org/wiki/Chamorro_language)
     * ([cha][LanguageAlpha3Code.cha]).
     */
    CH,

    /**
     * [Corsican](http://en.wikipedia.org/wiki/Corsican_language)
     * ([cos][LanguageAlpha3Code.cos]).
     */
    CO,

    /**
     * [Cree](http://en.wikipedia.org/wiki/Cree_language)
     * ([cre][LanguageAlpha3Code.cre]).
     */
    CR,

    /**
     * [Czech](http://en.wikipedia.org/wiki/Czech_language)
     * ([ces][LanguageAlpha3Code.ce S], [cze][LanguageAlpha3Code.cze]).
     */
    CS,

    /**
     * [Church Slavonic](http://en.wikipedia.org/wiki/Old_Church_Slavonic)
     * ([chu][LanguageAlpha3Code.chu]).
     */
    CU,

    /**
     * [Chuvash](http://en.wikipedia.org/wiki/Chuvash_language)
     * ([chv][LanguageAlpha3Code.chv]).
     */
    CV,

    /**
     * [Welsh](http://en.wikipedia.org/wiki/Welsh_language)
     * ([cym][LanguageAlpha3Code.cy M], [wel][LanguageAlpha3Code.wel]).
     */
    CY,

    /**
     * [Danish](http://en.wikipedia.org/wiki/Danish_language)
     * ([dan][LanguageAlpha3Code.dan]).
     */
    DA,

    /**
     * [German](http://en.wikipedia.org/wiki/German_language)
     * ([deu][LanguageAlpha3Code.de U], [ger][LanguageAlpha3Code.ger]).
     */
    DE,

    /**
     * [Dhivehi](http://en.wikipedia.org/wiki/Dhivehi_language)
     * ([div][LanguageAlpha3Code.div]).
     */
    DV,

    /**
     * [Dzongkha](http://en.wikipedia.org/wiki/Dzongkha_language)
     * ([dzo][LanguageAlpha3Code.dzo]).
     */
    DZ,

    /**
     * [Ewe](http://en.wikipedia.org/wiki/Ewe_language)
     * ([ewe][LanguageAlpha3Code.ewe]).
     */
    EE,

    /**
     * [Greek](http://en.wikipedia.org/wiki/Greek_language)
     * ([ell][LanguageAlpha3Code.el L], [gre][LanguageAlpha3Code.gre]).
     */
    EL,

    /**
     * [English](http://en.wikipedia.org/wiki/English_language)
     * ([eng][LanguageAlpha3Code.eng]).
     */
    EN,

    /**
     * [Esperanto](http://en.wikipedia.org/wiki/Esperanto)
     * ([epo][LanguageAlpha3Code.epo]).
     */
    EO,

    /**
     * [Spanish](http://en.wikipedia.org/wiki/Spanish_language)
     * ([spa][LanguageAlpha3Code.spa]).
     */
    ES,

    /**
     * [Estonian](http://en.wikipedia.org/wiki/Estonian_language)
     * ([est][LanguageAlpha3Code.est]).
     */
    ET,

    /**
     * [Basque](http://en.wikipedia.org/wiki/Basque_language)
     * ([eus][LanguageAlpha3Code.eu S], [baq][LanguageAlpha3Code.baq]).
     */
    EU,

    /**
     * [Persian](http://en.wikipedia.org/wiki/Persian_language)
     * ([fas][LanguageAlpha3Code.fa S], [per][LanguageAlpha3Code.per]).
     */
    FA,

    /**
     * [Fula](http://en.wikipedia.org/wiki/Fula_language)
     * ([ful][LanguageAlpha3Code.ful]).
     */
    FF,

    /**
     * [Finnish](http://en.wikipedia.org/wiki/Finnish_language)
     * ([fin][LanguageAlpha3Code.fin]).
     */
    FI,

    /**
     * [Fijian](http://en.wikipedia.org/wiki/Fijian_language)
     * ([fij][LanguageAlpha3Code.fij]).
     */
    FJ,

    /**
     * [Faroese](http://en.wikipedia.org/wiki/Faroese_language)
     * ([fao][LanguageAlpha3Code.fao]).
     */
    FO,

    /**
     * [French](http://en.wikipedia.org/wiki/French_language)
     * ([fra][LanguageAlpha3Code.fr A], [fre][LanguageAlpha3Code.fre]).
     */
    FR,

    /**
     * [West Frisian](http://en.wikipedia.org/wiki/West_Frisian_language)
     * ([fry][LanguageAlpha3Code.fry]).
     */
    FY,

    /**
     * [Irish](http://en.wikipedia.org/wiki/Irish_language)
     * ([gle][LanguageAlpha3Code.gle]).
     */
    GA,

    /**
     * [Scottish Gaelic](http://en.wikipedia.org/wiki/Scottish_Gaelic_language)
     * ([gla][LanguageAlpha3Code.gla]).
     */
    GD,

    /**
     * [Galician](http://en.wikipedia.org/wiki/Galician_language)
     * ([glg][LanguageAlpha3Code.glg]).
     */
    GL,

    /**
     * [Guaran](http://en.wikipedia.org/wiki/Guaran%C3%AD_language)
     * ([grn][LanguageAlpha3Code.grn]).
     */
    GN,

    /**
     * [Gujarati](http://en.wikipedia.org/wiki/Gujarati_language)
     * ([guj][LanguageAlpha3Code.guj]).
     */
    GU,

    /**
     * [Manx](http://en.wikipedia.org/wiki/Manx_language)
     * ([glv][LanguageAlpha3Code.glv]).
     */
    GV,

    /**
     * [Hausa](http://en.wikipedia.org/wiki/Hausa_language)
     * ([hau][LanguageAlpha3Code.hau]).
     */
    HA,

    /**
     * [Hebrew](http://en.wikipedia.org/wiki/Hebrew_language)
     * ([heb][LanguageAlpha3Code.heb]).
     */
    HE,

    /**
     * [Hindi](http://en.wikipedia.org/wiki/Hindi)
     * ([hin][LanguageAlpha3Code.hin]).
     */
    HI,

    /**
     * [Hiri Motu](http://en.wikipedia.org/wiki/Hiri_Motu_language)
     * ([hmo][LanguageAlpha3Code.hmo]).
     */
    HO,

    /**
     * [Croatian](http://en.wikipedia.org/wiki/Croatian_language)
     * ([hrv][LanguageAlpha3Code.hrv]).
     */
    HR,

    /**
     * [Haitian](http://en.wikipedia.org/wiki/Haitian_Creole_language)
     * ([hat][LanguageAlpha3Code.hat]).
     */
    HT,

    /**
     * [Hungarian](http://en.wikipedia.org/wiki/Hungarian_language)
     * ([hun][LanguageAlpha3Code.hun]).
     */
    HU,

    /**
     * [Armenian](http://en.wikipedia.org/wiki/Armenian_language)
     * ([hye][LanguageAlpha3Code.hy E], [arm][LanguageAlpha3Code.arm]).
     */
    HY,

    /**
     * [Herero](http://en.wikipedia.org/wiki/Herero_language)
     * ([her][LanguageAlpha3Code.her]).
     */
    HZ,

    /**
     * [Interlingua](http://en.wikipedia.org/wiki/Interlingua)
     * ([ina][LanguageAlpha3Code.ina]).
     */
    IA,

    /**
     * [Indonesian](http://en.wikipedia.org/wiki/Indonesian_language)
     * ([ind][LanguageAlpha3Code.ind]).
     */
    ID,

    /**
     * [Interlingue](http://en.wikipedia.org/wiki/Interlingue_language)
     * ([ile][LanguageAlpha3Code.ile]).
     */
    IE,

    /**
     * [Igbo](http://en.wikipedia.org/wiki/Igbo_language)
     * ([ibo][LanguageAlpha3Code.ibo]).
     */
    IG,

    /**
     * [Nuosu](http://en.wikipedia.org/wiki/Nuosu_language)
     * ([iii][LanguageAlpha3Code.iii]).
     */
    II,

    /**
     * [Inupiaq](http://en.wikipedia.org/wiki/Inupiaq_language)
     * ([ipk][LanguageAlpha3Code.ipk]).
     */
    IK,

    /**
     * [Ido](http://en.wikipedia.org/wiki/Ido)
     * ([ido][LanguageAlpha3Code.ido]).
     */
    IO,

    /**
     * [Icelandic](http://en.wikipedia.org/wiki/Icelandic_language)
     * ([isl][LanguageAlpha3Code.is L], [ice][LanguageAlpha3Code.ice]).
     */
    IS,

    /**
     * [Italian](http://en.wikipedia.org/wiki/Italian_language)
     * ([ita][LanguageAlpha3Code.ita]).
     */
    IT,

    /**
     * [Inuktitut](http://en.wikipedia.org/wiki/Inuktitut)
     * ([iku][LanguageAlpha3Code.iku]).
     */
    IU,

    /**
     * [Japanese](http://en.wikipedia.org/wiki/Japanese_language)
     * ([jpn][LanguageAlpha3Code.jpn]).
     */
    JA,

    /**
     * [Javanese](http://en.wikipedia.org/wiki/Javanese_language)
     * ([jav][LanguageAlpha3Code.jav]).
     */
    JV,

    /**
     * [Georgian](http://en.wikipedia.org/wiki/Georgian_language)
     * ([kat][LanguageAlpha3Code.ka T], [geo][LanguageAlpha3Code.geo]).
     */
    KA,

    /**
     * [Kongo](http://en.wikipedia.org/wiki/Kongo_language)
     * ([kon][LanguageAlpha3Code.kon]).
     */
    KG,

    /**
     * [Kikuyu](http://en.wikipedia.org/wiki/Gikuyu_language)
     * ([kik][LanguageAlpha3Code.kik]).
     */
    KI,

    /**
     * [Kwanyama](http://en.wikipedia.org/wiki/Kwanyama)
     * ([kua][LanguageAlpha3Code.kua]).
     */
    KJ,

    /**
     * [Kazakh](http://en.wikipedia.org/wiki/Kazakh_language)
     * ([kaz][LanguageAlpha3Code.kaz]).
     */
    KK,

    /**
     * [Kalaallisut](http://en.wikipedia.org/wiki/Kalaallisut_language)
     * ([kal][LanguageAlpha3Code.kal]).
     */
    KL,

    /**
     * [Khmer](http://en.wikipedia.org/wiki/Khmer_language)
     * ([khm][LanguageAlpha3Code.khm]).
     */
    KM,

    /**
     * [Kannada](http://en.wikipedia.org/wiki/Kannada_language)
     * ([kan][LanguageAlpha3Code.kan]).
     */
    KN,

    /**
     * [Korean](http://en.wikipedia.org/wiki/Korean_language)
     * ([kor][LanguageAlpha3Code.kor]).
     */
    KO,

    /**
     * [Kanuri](http://en.wikipedia.org/wiki/Kanuri_language)
     * ([kau][LanguageAlpha3Code.kau]).
     */
    KR,

    /**
     * [Kashmiri](http://en.wikipedia.org/wiki/Kashmiri_language)
     * ([kas][LanguageAlpha3Code.kas]).
     */
    KS,

    /**
     * [Kurdish](http://en.wikipedia.org/wiki/Kurdish_language)
     * ([kur][LanguageAlpha3Code.kur]).
     */
    KU,

    /**
     * [Komi](http://en.wikipedia.org/wiki/Komi_language)
     * ([kom][LanguageAlpha3Code.kom]).
     */
    KV,

    /**
     * [Cornish](http://en.wikipedia.org/wiki/Cornish_language)
     * ([cor][LanguageAlpha3Code.cor]).
     */
    KW,

    /**
     * [Kyrgyz](http://en.wikipedia.org/wiki/Kyrgyz_language)
     * ([kir][LanguageAlpha3Code.kir]).
     */
    KY,

    /**
     * [Latin](http://en.wikipedia.org/wiki/Latin)
     * ([lat][LanguageAlpha3Code.lat]).
     */
    LA,

    /**
     * [Luxembourgish](http://en.wikipedia.org/wiki/Luxembourgish_language)
     * ([ltz][LanguageAlpha3Code.ltz]).
     */
    LB,

    /**
     * [Ganda](http://en.wikipedia.org/wiki/Luganda)
     * ([lug][LanguageAlpha3Code.lug]).
     */
    LG,

    /**
     * [Limburgish](http://en.wikipedia.org/wiki/Limburgish_language)
     * ([lim][LanguageAlpha3Code.lim]).
     */
    LI,

    /**
     * [Lingala](http://en.wikipedia.org/wiki/Lingala_language)
     * ([lin][LanguageAlpha3Code.lin]).
     */
    LN,

    /**
     * [Lao](http://en.wikipedia.org/wiki/Lao_language)
     * ([lao][LanguageAlpha3Code.lao]).
     */
    LO,

    /**
     * [Lithuanian](http://en.wikipedia.org/wiki/Lithuanian_language)
     * ([lit][LanguageAlpha3Code.lit]).
     */
    LT,

    /**
     * [Luba-Katanga](http://en.wikipedia.org/wiki/Tshiluba_language)
     * ([lub][LanguageAlpha3Code.lub]).
     */
    LU,

    /**
     * [Latvian](http://en.wikipedia.org/wiki/Latvian_language)
     * ([lav][LanguageAlpha3Code.lav]).
     */
    LV,

    /**
     * [Malagasy](http://en.wikipedia.org/wiki/Malagasy_language)
     * ([mlg][LanguageAlpha3Code.mlg]).
     */
    MG,

    /**
     * [Marshallese](http://en.wikipedia.org/wiki/Marshallese_language)
     * ([mah][LanguageAlpha3Code.mah]).
     */
    MH,

    /**
     * [M&#257;ori](http://en.wikipedia.org/wiki/M%C4%81ori_language)
     * ([mir][LanguageAlpha3Code.mr I], [mao][LanguageAlpha3Code.mao]).
     */
    MI,

    /**
     * [Macedonian](http://en.wikipedia.org/wiki/Macedonian_language)
     * ([mkd][LanguageAlpha3Code.mk D], [mac][LanguageAlpha3Code.mac])).
     */
    MK,

    /**
     * [Malayalam](http://en.wikipedia.org/wiki/Malayalam_language)
     * ([mal][LanguageAlpha3Code.mal]).
     */
    ML,

    /**
     * [Mongolian](http://en.wikipedia.org/wiki/Mongolian_language)
     * ([mon][LanguageAlpha3Code.mon]).
     */
    MN,

    /**
     * [Marathi](http://en.wikipedia.org/wiki/Marathi_language)
     * ([mar][LanguageAlpha3Code.mar]).
     */
    MR,

    /**
     * [Malay](http://en.wikipedia.org/wiki/Malay_language)
     * ([msa][LanguageAlpha3Code.ms A], [may][LanguageAlpha3Code.may]).
     */
    MS,

    /**
     * [Maltese](http://en.wikipedia.org/wiki/Maltese_language)
     * ([mlt][LanguageAlpha3Code.mlt]).
     */
    MT,

    /**
     * [Burmese](http://en.wikipedia.org/wiki/Burmese_language)
     * ([may][LanguageAlpha3Code.my A], [bur][LanguageAlpha3Code.bur]).
     */
    MY,

    /**
     * [Nauru](http://en.wikipedia.org/wiki/Nauruan_language)
     * ([nau][LanguageAlpha3Code.nau]).
     */
    NA,

    /**
     * [Norwegian Bokml](http://en.wikipedia.org/wiki/Bokm%C3%A5l)
     * ([nob][LanguageAlpha3Code.nob]).
     */
    NB,

    /**
     * [Northern Ndebele](http://en.wikipedia.org/wiki/Northern_Ndebele_language)
     * ([nde][LanguageAlpha3Code.nde]).
     */
    ND,

    /**
     * [Nepali](http://en.wikipedia.org/wiki/Nepali_language)
     * ([nep][LanguageAlpha3Code.nep]).
     */
    NE,

    /**
     * [Ndonga](http://en.wikipedia.org/wiki/Ndonga)
     * ([ndo][LanguageAlpha3Code.ndo]).
     */
    NG,

    /**
     * [Dutch](http://en.wikipedia.org/wiki/Dutch_language)
     * ([nld][LanguageAlpha3Code.nl D], [dut][LanguageAlpha3Code.dut]).
     */
    NL,

    /**
     * [Norwegian Nynorsk](http://en.wikipedia.org/wiki/Nynorsk)
     * ([nno][LanguageAlpha3Code.nno]).
     */
    NN,

    /**
     * [Norwegian](http://en.wikipedia.org/wiki/Norwegian_language)
     * ([nor][LanguageAlpha3Code.nor]).
     *
     * @see [Sprkkoder for POSIX locale i Norge](http://i18n.skolelinux.no/localekoder.txt)
     *
     * @see [Red Hat Bugzilla – Bug 532487 Legacy Norwegian locale
     ](https://bugzilla.redhat.com/show_bug.cgi?id=532487) */
    NO,

    /**
     * [Southern Ndebele](http://en.wikipedia.org/wiki/Southern_Ndebele_language)
     * ([nbl][LanguageAlpha3Code.nbl]).
     */
    NR,

    /**
     * [Navajo](http://en.wikipedia.org/wiki/Navajo_language)
     * ([nav][LanguageAlpha3Code.nav]).
     */
    NV,

    /**
     * [Chichewa](http://en.wikipedia.org/wiki/Chichewa_language)
     * ([nya][LanguageAlpha3Code.nya]).
     */
    NY,

    /**
     * [Occitan](http://en.wikipedia.org/wiki/Occitan_language)
     * ([oci][LanguageAlpha3Code.oci]).
     */
    OC,

    /**
     * [Ojibwe](http://en.wikipedia.org/wiki/Ojibwe_language)
     * ([oji][LanguageAlpha3Code.oji]).
     */
    OJ,

    /**
     * [Oromo](http://en.wikipedia.org/wiki/Oromo_language)
     * ([orm][LanguageAlpha3Code.orm]).
     */
    OM,

    /**
     * [Oriya](http://en.wikipedia.org/wiki/Oriya_language)
     * ([ori][LanguageAlpha3Code.ori]).
     */
    OR,

    /**
     * [Ossetian](http://en.wikipedia.org/wiki/Ossetic_language)
     * ([oss][LanguageAlpha3Code.oss]).
     */
    OS,

    /**
     * [Punjabi](http://en.wikipedia.org/wiki/Punjabi_language)
     * ([pan][LanguageAlpha3Code.pan]).
     */
    PA,

    /**
     * [P&#257;li](http://en.wikipedia.org/wiki/P%C4%81li_language)
     * ([pli][LanguageAlpha3Code.pli]).
     */
    PI,

    /**
     * [Polish](http://en.wikipedia.org/wiki/Polish_language)
     * ([pol][LanguageAlpha3Code.pol]).
     */
    PL,

    /**
     * [Pashto](http://en.wikipedia.org/wiki/Pashto_language)
     * ([pus][LanguageAlpha3Code.pus]).
     */
    PS,

    /**
     * [Portuguese](http://en.wikipedia.org/wiki/Portuguese_language)
     * ([por][LanguageAlpha3Code.por]).
     */
    PT,

    /**
     * [Quechua](http://en.wikipedia.org/wiki/Quechua_language)
     * ([que][LanguageAlpha3Code.que]).
     */
    QU,

    /**
     * [Romansh](http://en.wikipedia.org/wiki/Romansh_language)
     * ([roh][LanguageAlpha3Code.roh]).
     */
    RM,

    /**
     * [Kirundi](http://en.wikipedia.org/wiki/Kirundi)
     * ([run][LanguageAlpha3Code.run]).
     */
    RN,

    /**
     * [Romanian](http://en.wikipedia.org/wiki/Romanian_language)
     * ([ron][LanguageAlpha3Code.ro N], [rum][LanguageAlpha3Code.rum]).
     */
    RO,

    /**
     * [Russian](http://en.wikipedia.org/wiki/Russian_language)
     * ([run][LanguageAlpha3Code.run]).
     */
    RU,

    /**
     * [Kinyarwanda](http://en.wikipedia.org/wiki/Kinyarwanda)
     * ([kin][LanguageAlpha3Code.kin]).
     */
    RW,

    /**
     * [Sanskrit](http://en.wikipedia.org/wiki/Sanskrit)
     * ([san][LanguageAlpha3Code.san]).
     */
    SA,

    /**
     * [Sardinian](http://en.wikipedia.org/wiki/Sardinian_language)
     * ([srd][LanguageAlpha3Code.srd]).
     */
    SC,

    /**
     * [Sindhi](http://en.wikipedia.org/wiki/Sindhi_language)
     * ([snd][LanguageAlpha3Code.snd]).
     */
    SD,

    /**
     * [Northern Sami](http://en.wikipedia.org/wiki/Northern_Sami)
     * ([sme][LanguageAlpha3Code.sme]).
     */
    SE,

    /**
     * [Sango](http://en.wikipedia.org/wiki/Sango_language)
     * ([sag][LanguageAlpha3Code.sag]).
     */
    SG,

    /**
     * [Sinhala](http://en.wikipedia.org/wiki/Sinhala_language)
     * ([sin][LanguageAlpha3Code.sin]).
     */
    SI,

    /**
     * [Slovak](http://en.wikipedia.org/wiki/Slovak_language)
     * ([slk][LanguageAlpha3Code.sl K], [slo][LanguageAlpha3Code.slo]).
     */
    SK,

    /**
     * [Slovene](http://en.wikipedia.org/wiki/Slovene_language)
     * ([slv][LanguageAlpha3Code.slv]).
     */
    SL,

    /**
     * [Samoan](http://en.wikipedia.org/wiki/Samoan_language)
     * ([smo][LanguageAlpha3Code.smo]).
     */
    SM,

    /**
     * [Shona](http://en.wikipedia.org/wiki/Shona_language)
     * ([sna][LanguageAlpha3Code.sna]).
     */
    SN,

    /**
     * [Somali](http://en.wikipedia.org/wiki/Somali_language)
     * ([som][LanguageAlpha3Code.som]).
     */
    SO,

    /**
     * [Albanian](http://en.wikipedia.org/wiki/Albanian_language)
     * ([sqi][LanguageAlpha3Code.sq I], [alb][LanguageAlpha3Code.alb]).
     */
    SQ,

    /**
     * [Serbian](http://en.wikipedia.org/wiki/Serbian_language)
     * ([srp][LanguageAlpha3Code.srp]).
     */
    SR,

    /**
     * [Swati](http://en.wikipedia.org/wiki/Swati_language)
     * ([ssw][LanguageAlpha3Code.ssw]).
     */
    SS,

    /**
     * [Southern Sotho](http://en.wikipedia.org/wiki/Sotho_language)
     * ([sot][LanguageAlpha3Code.sot]).
     */
    ST,

    /**
     * [Sundanese](http://en.wikipedia.org/wiki/Sundanese_language)
     * ([sun][LanguageAlpha3Code.sun]).
     */
    SU,

    /**
     * [Swedish](http://en.wikipedia.org/wiki/Swedish_language)
     * ([swe][LanguageAlpha3Code.swe]).
     */
    SV,

    /**
     * [Swahili](http://en.wikipedia.org/wiki/Swahili_language)
     * ([swa][LanguageAlpha3Code.swa]).
     */
    SW,

    /**
     * [Tamil](http://en.wikipedia.org/wiki/Tamil_language)
     * ([tam][LanguageAlpha3Code.tam]).
     */
    TA,

    /**
     * [Telugu](http://en.wikipedia.org/wiki/Telugu_language)
     * ([tel][LanguageAlpha3Code.tel]).
     */
    TE,

    /**
     * [Tajik](http://en.wikipedia.org/wiki/Tajik_language)
     * ([tgk][LanguageAlpha3Code.tgk]).
     */
    TG,

    /**
     * [Thai](http://en.wikipedia.org/wiki/Thai_language)
     * ([tha][LanguageAlpha3Code.tha]).
     */
    TH,

    /**
     * [Tigrinya](http://en.wikipedia.org/wiki/Tigrinya_language)
     * ([tir][LanguageAlpha3Code.tir]).
     */
    TI,

    /**
     * [Turkmen](http://en.wikipedia.org/wiki/Turkmen_language)
     * ([tuk][LanguageAlpha3Code.tuk]).
     */
    TK,

    /**
     * [Tagalog](http://en.wikipedia.org/wiki/Tagalog_language)
     * ([tgl][LanguageAlpha3Code.tgl]).
     */
    TL,

    /**
     * [Tswana](http://en.wikipedia.org/wiki/Tswana_language)
     * ([tsn][LanguageAlpha3Code.tsn]).
     */
    TN,

    /**
     * [Tongan](http://en.wikipedia.org/wiki/Tongan_language)
     * ([ton][LanguageAlpha3Code.ton]).
     */
    TO,

    /**
     * [Turkish](http://en.wikipedia.org/wiki/Turkish_language)
     * ([tur][LanguageAlpha3Code.tur]).
     */
    TR,

    /**
     * [Tsonga](http://en.wikipedia.org/wiki/Tsonga_language)
     * ([tso][LanguageAlpha3Code.tso]).
     */
    TS,

    /**
     * [Tatar](http://en.wikipedia.org/wiki/Tatar_language)
     * ([tat][LanguageAlpha3Code.tat]).
     */
    TT,

    /**
     * [Twi](http://en.wikipedia.org/wiki/Twi)
     * ([twi][LanguageAlpha3Code.twi]).
     */
    TW,

    /**
     * [Tahitian](http://en.wikipedia.org/wiki/Tahitian_language)
     * ([tah][LanguageAlpha3Code.tah]).
     */
    TY,

    /**
     * [Uighur](http://en.wikipedia.org/wiki/Uyghur_language)
     * ([uig][LanguageAlpha3Code.uig]).
     */
    UG,

    /**
     * [Ukrainian](http://en.wikipedia.org/wiki/Ukrainian_language)
     * ([ukr][LanguageAlpha3Code.ukr]).
     */
    UK,

    /**
     * [Urdu](http://en.wikipedia.org/wiki/Urdu)
     * ([urd][LanguageAlpha3Code.urd]).
     */
    UR,

    /**
     * [Uzbek](http://en.wikipedia.org/wiki/Uzbek_language)
     * ([uzb][LanguageAlpha3Code.uzb]).
     */
    UZ,

    /**
     * [Venda](http://en.wikipedia.org/wiki/Venda_language)
     * ([ven][LanguageAlpha3Code.ven]).
     */
    VE,

    /**
     * [Vietnamese](http://en.wikipedia.org/wiki/Vietnamese_language)
     * ([vie][LanguageAlpha3Code.vie]).
     */
    VI,

    /**
     * [Volapk](http://en.wikipedia.org/wiki/Volap%C3%BCk)
     * ([vol][LanguageAlpha3Code.vol]).
     */
    VO,

    /**
     * [Walloon](http://en.wikipedia.org/wiki/Walloon_language)
     * ([wln][LanguageAlpha3Code.wln]).
     */
    WA,

    /**
     * [Wolof](http://en.wikipedia.org/wiki/Wolof_language)
     * ([wol][LanguageAlpha3Code.wol]).
     */
    WO,

    /**
     * [Xhosa](http://en.wikipedia.org/wiki/Xhosa_language)
     * ([xho][LanguageAlpha3Code.xho]).
     */
    XH,

    /**
     * [Yiddish](http://en.wikipedia.org/wiki/Yiddish_language)
     * ([yid][LanguageAlpha3Code.yid]).
     */
    YI,

    /**
     * [Yoruba](http://en.wikipedia.org/wiki/Yoruba_language)
     * ([yor][LanguageAlpha3Code.yor]).
     */
    YO,

    /**
     * [Zhuang](http://en.wikipedia.org/wiki/Zhuang_languages)
     * ([zha][LanguageAlpha3Code.zha]).
     */
    ZA,

    /**
     * [Chinese](http://en.wikipedia.org/wiki/Chinese_language)
     * ([zho][LanguageAlpha3Code.zh O], [chi][LanguageAlpha3Code.chi]).
     */
    ZH
}
