/* Spotify Web API, Kotlin Wrapper; MIT License, 2017-2020; Original author: Adam Ratzman */
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
 * <span style="color: darkgreen;">// For example, "[ar] Arabic" is printed.</span>
 * System.out.format(<span style="color: darkred;">"[%s] %s\n"</span>, code, code.[.getName]);
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
enum class Language {
    /**
     * [Afar](http://en.wikipedia.org/wiki/Afar_language)
     * ([aar][LanguageAlpha3Code.aar]).
     */
    aa,
    /**
     * [Abkhaz](http://en.wikipedia.org/wiki/Abkhaz_language)
     * ([abk][LanguageAlpha3Code.abk]).
     */
    ab,
    /**
     * [Avestan](http://en.wikipedia.org/wiki/Avestan_language)
     * ([ave][LanguageAlpha3Code.ave]).
     */
    ae,
    /**
     * [Afrikaans](http://en.wikipedia.org/wiki/Afrikaans_language)
     * ([afr][LanguageAlpha3Code.afr]).
     */
    af,
    /**
     * [Akan](http://en.wikipedia.org/wiki/Akan_language)
     * ([aka][LanguageAlpha3Code.aka]).
     */
    ak,
    /**
     * [Amharic](http://en.wikipedia.org/wiki/Amharic_language)
     * ([amh][LanguageAlpha3Code.amh]).
     */
    am,
    /**
     * [Aragonese](http://en.wikipedia.org/wiki/Aragonese_language)
     * ([arg][LanguageAlpha3Code.arg]).
     */
    an,
    /**
     * [Arabic](http://en.wikipedia.org/wiki/Arabic_language)
     * ([ara][LanguageAlpha3Code.ara]).
     */
    ar,
    /**
     * [Assamese](http://en.wikipedia.org/wiki/Assamese_language)
     * ([asm][LanguageAlpha3Code.asm]).
     */
    `as`,
    /**
     * [Avaric](http://en.wikipedia.org/wiki/Avar_language)
     * ([ava][LanguageAlpha3Code.ava]).
     */
    av,
    /**
     * [Aymara](http://en.wikipedia.org/wiki/Aymara_language)
     * ([aym][LanguageAlpha3Code.aym]).
     */
    ay,
    /**
     * [Azerbaijani](http://en.wikipedia.org/wiki/Azerbaijani_language)
     * ([aze][LanguageAlpha3Code.aze]).
     */
    az,
    /**
     * [Bashkir](http://en.wikipedia.org/wiki/Bashkir_language)
     * ([bak][LanguageAlpha3Code.bak]).
     */
    ba,
    /**
     * [Belarusian](http://en.wikipedia.org/wiki/Belarusian_language)
     * ([bel][LanguageAlpha3Code.bel]).
     */
    be,
    /**
     * [Bulgarian](http://en.wikipedia.org/wiki/Bulgarian_language)
     * ([bul][LanguageAlpha3Code.bul]).
     */
    bg,
    /**
     * [Bihari](http://en.wikipedia.org/wiki/Bihari_languages)
     * ([bih][LanguageAlpha3Code.bih]).
     */
    bh,
    /**
     * [Bislama](http://en.wikipedia.org/wiki/Bislama_language)
     * ([bis][LanguageAlpha3Code.bis]).
     */
    bi,
    /**
     * [Bambara](http://en.wikipedia.org/wiki/Bambara_language)
     * ([bam][LanguageAlpha3Code.bam]).
     */
    bm,
    /**
     * [Bengali](http://en.wikipedia.org/wiki/Bengali_language)
     * ([ben][LanguageAlpha3Code.ben]).
     */
    bn,
    /**
     * [Tibetan](http://en.wikipedia.org/wiki/Standard_Tibetan)
     * ([bod][LanguageAlpha3Code.bod], [tib][LanguageAlpha3Code.tib]).
     */
    bo,
    /**
     * [Breton](http://en.wikipedia.org/wiki/Breton_language)
     * ([bre][LanguageAlpha3Code.bre]).
     */
    br,
    /**
     * [Bosnian](http://en.wikipedia.org/wiki/Bosnian_language)
     * ([bos][LanguageAlpha3Code.bos]).
     */
    bs,
    /**
     * [Catalan](http://en.wikipedia.org/wiki/Catalan_language)
     * ([cat][LanguageAlpha3Code.cat]).
     */
    ca,
    /**
     * [Chechen](http://en.wikipedia.org/wiki/Chechen_language)
     * ([che][LanguageAlpha3Code.che]).
     */
    ce,
    /**
     * [Chamorro](http://en.wikipedia.org/wiki/Chamorro_language)
     * ([cha][LanguageAlpha3Code.cha]).
     */
    ch,
    /**
     * [Corsican](http://en.wikipedia.org/wiki/Corsican_language)
     * ([cos][LanguageAlpha3Code.cos]).
     */
    co,
    /**
     * [Cree](http://en.wikipedia.org/wiki/Cree_language)
     * ([cre][LanguageAlpha3Code.cre]).
     */
    cr,
    /**
     * [Czech](http://en.wikipedia.org/wiki/Czech_language)
     * ([ces][LanguageAlpha3Code.ces], [cze][LanguageAlpha3Code.cze]).
     */
    cs,
    /**
     * [Church Slavonic](http://en.wikipedia.org/wiki/Old_Church_Slavonic)
     * ([chu][LanguageAlpha3Code.chu]).
     */
    cu,
    /**
     * [Chuvash](http://en.wikipedia.org/wiki/Chuvash_language)
     * ([chv][LanguageAlpha3Code.chv]).
     */
    cv,
    /**
     * [Welsh](http://en.wikipedia.org/wiki/Welsh_language)
     * ([cym][LanguageAlpha3Code.cym], [wel][LanguageAlpha3Code.wel]).
     */
    cy,
    /**
     * [Danish](http://en.wikipedia.org/wiki/Danish_language)
     * ([dan][LanguageAlpha3Code.dan]).
     */
    da,
    /**
     * [German](http://en.wikipedia.org/wiki/German_language)
     * ([deu][LanguageAlpha3Code.deu], [ger][LanguageAlpha3Code.ger]).
     */
    de,
    /**
     * [Dhivehi](http://en.wikipedia.org/wiki/Dhivehi_language)
     * ([div][LanguageAlpha3Code.div]).
     */
    dv,
    /**
     * [Dzongkha](http://en.wikipedia.org/wiki/Dzongkha_language)
     * ([dzo][LanguageAlpha3Code.dzo]).
     */
    dz,
    /**
     * [Ewe](http://en.wikipedia.org/wiki/Ewe_language)
     * ([ewe][LanguageAlpha3Code.ewe]).
     */
    ee,
    /**
     * [Greek](http://en.wikipedia.org/wiki/Greek_language)
     * ([ell][LanguageAlpha3Code.ell], [gre][LanguageAlpha3Code.gre]).
     */
    el,
    /**
     * [English](http://en.wikipedia.org/wiki/English_language)
     * ([eng][LanguageAlpha3Code.eng]).
     */
    en,
    /**
     * [Esperanto](http://en.wikipedia.org/wiki/Esperanto)
     * ([epo][LanguageAlpha3Code.epo]).
     */
    eo,
    /**
     * [Spanish](http://en.wikipedia.org/wiki/Spanish_language)
     * ([spa][LanguageAlpha3Code.spa]).
     */
    es,
    /**
     * [Estonian](http://en.wikipedia.org/wiki/Estonian_language)
     * ([est][LanguageAlpha3Code.est]).
     */
    et,
    /**
     * [Basque](http://en.wikipedia.org/wiki/Basque_language)
     * ([eus][LanguageAlpha3Code.eus], [baq][LanguageAlpha3Code.baq]).
     */
    eu,
    /**
     * [Persian](http://en.wikipedia.org/wiki/Persian_language)
     * ([fas][LanguageAlpha3Code.fas], [per][LanguageAlpha3Code.per]).
     */
    fa,
    /**
     * [Fula](http://en.wikipedia.org/wiki/Fula_language)
     * ([ful][LanguageAlpha3Code.ful]).
     */
    ff,
    /**
     * [Finnish](http://en.wikipedia.org/wiki/Finnish_language)
     * ([fin][LanguageAlpha3Code.fin]).
     */
    fi,
    /**
     * [Fijian](http://en.wikipedia.org/wiki/Fijian_language)
     * ([fij][LanguageAlpha3Code.fij]).
     */
    fj,
    /**
     * [Faroese](http://en.wikipedia.org/wiki/Faroese_language)
     * ([fao][LanguageAlpha3Code.fao]).
     */
    fo,
    /**
     * [French](http://en.wikipedia.org/wiki/French_language)
     * ([fra][LanguageAlpha3Code.fra], [fre][LanguageAlpha3Code.fre]).
     */
    fr,
    /**
     * [West Frisian](http://en.wikipedia.org/wiki/West_Frisian_language)
     * ([fry][LanguageAlpha3Code.fry]).
     */
    fy,
    /**
     * [Irish](http://en.wikipedia.org/wiki/Irish_language)
     * ([gle][LanguageAlpha3Code.gle]).
     */
    ga,
    /**
     * [Scottish Gaelic](http://en.wikipedia.org/wiki/Scottish_Gaelic_language)
     * ([gla][LanguageAlpha3Code.gla]).
     */
    gd,
    /**
     * [Galician](http://en.wikipedia.org/wiki/Galician_language)
     * ([glg][LanguageAlpha3Code.glg]).
     */
    gl,
    /**
     * [Guaran](http://en.wikipedia.org/wiki/Guaran%C3%AD_language)
     * ([grn][LanguageAlpha3Code.grn]).
     */
    gn,
    /**
     * [Gujarati](http://en.wikipedia.org/wiki/Gujarati_language)
     * ([guj][LanguageAlpha3Code.guj]).
     */
    gu,
    /**
     * [Manx](http://en.wikipedia.org/wiki/Manx_language)
     * ([glv][LanguageAlpha3Code.glv]).
     */
    gv,
    /**
     * [Hausa](http://en.wikipedia.org/wiki/Hausa_language)
     * ([hau][LanguageAlpha3Code.hau]).
     */
    ha,
    /**
     * [Hebrew](http://en.wikipedia.org/wiki/Hebrew_language)
     * ([heb][LanguageAlpha3Code.heb]).
     */
    he,
    /**
     * [Hindi](http://en.wikipedia.org/wiki/Hindi)
     * ([hin][LanguageAlpha3Code.hin]).
     */
    hi,
    /**
     * [Hiri Motu](http://en.wikipedia.org/wiki/Hiri_Motu_language)
     * ([hmo][LanguageAlpha3Code.hmo]).
     */
    ho,
    /**
     * [Croatian](http://en.wikipedia.org/wiki/Croatian_language)
     * ([hrv][LanguageAlpha3Code.hrv]).
     */
    hr,
    /**
     * [Haitian](http://en.wikipedia.org/wiki/Haitian_Creole_language)
     * ([hat][LanguageAlpha3Code.hat]).
     */
    ht,
    /**
     * [Hungarian](http://en.wikipedia.org/wiki/Hungarian_language)
     * ([hun][LanguageAlpha3Code.hun]).
     */
    hu,
    /**
     * [Armenian](http://en.wikipedia.org/wiki/Armenian_language)
     * ([hye][LanguageAlpha3Code.hye], [arm][LanguageAlpha3Code.arm]).
     */
    hy,
    /**
     * [Herero](http://en.wikipedia.org/wiki/Herero_language)
     * ([her][LanguageAlpha3Code.her]).
     */
    hz,
    /**
     * [Interlingua](http://en.wikipedia.org/wiki/Interlingua)
     * ([ina][LanguageAlpha3Code.ina]).
     */
    ia,
    /**
     * [Indonesian](http://en.wikipedia.org/wiki/Indonesian_language)
     * ([ind][LanguageAlpha3Code.ind]).
     */
    id,
    /**
     * [Interlingue](http://en.wikipedia.org/wiki/Interlingue_language)
     * ([ile][LanguageAlpha3Code.ile]).
     */
    ie,
    /**
     * [Igbo](http://en.wikipedia.org/wiki/Igbo_language)
     * ([ibo][LanguageAlpha3Code.ibo]).
     */
    ig,
    /**
     * [Nuosu](http://en.wikipedia.org/wiki/Nuosu_language)
     * ([iii][LanguageAlpha3Code.iii]).
     */
    ii,
    /**
     * [Inupiaq](http://en.wikipedia.org/wiki/Inupiaq_language)
     * ([ipk][LanguageAlpha3Code.ipk]).
     */
    ik,
    /**
     * [Ido](http://en.wikipedia.org/wiki/Ido)
     * ([ido][LanguageAlpha3Code.ido]).
     */
    io,
    /**
     * [Icelandic](http://en.wikipedia.org/wiki/Icelandic_language)
     * ([isl][LanguageAlpha3Code.isl], [ice][LanguageAlpha3Code.ice]).
     */
    `is`,
    /**
     * [Italian](http://en.wikipedia.org/wiki/Italian_language)
     * ([ita][LanguageAlpha3Code.ita]).
     */
    it,
    /**
     * [Inuktitut](http://en.wikipedia.org/wiki/Inuktitut)
     * ([iku][LanguageAlpha3Code.iku]).
     */
    iu,
    /**
     * [Japanese](http://en.wikipedia.org/wiki/Japanese_language)
     * ([jpn][LanguageAlpha3Code.jpn]).
     */
    ja,
    /**
     * [Javanese](http://en.wikipedia.org/wiki/Javanese_language)
     * ([jav][LanguageAlpha3Code.jav]).
     */
    jv,
    /**
     * [Georgian](http://en.wikipedia.org/wiki/Georgian_language)
     * ([kat][LanguageAlpha3Code.kat], [geo][LanguageAlpha3Code.geo]).
     */
    ka,
    /**
     * [Kongo](http://en.wikipedia.org/wiki/Kongo_language)
     * ([kon][LanguageAlpha3Code.kon]).
     */
    kg,
    /**
     * [Kikuyu](http://en.wikipedia.org/wiki/Gikuyu_language)
     * ([kik][LanguageAlpha3Code.kik]).
     */
    ki,
    /**
     * [Kwanyama](http://en.wikipedia.org/wiki/Kwanyama)
     * ([kua][LanguageAlpha3Code.kua]).
     */
    kj,
    /**
     * [Kazakh](http://en.wikipedia.org/wiki/Kazakh_language)
     * ([kaz][LanguageAlpha3Code.kaz]).
     */
    kk,
    /**
     * [Kalaallisut](http://en.wikipedia.org/wiki/Kalaallisut_language)
     * ([kal][LanguageAlpha3Code.kal]).
     */
    kl,
    /**
     * [Khmer](http://en.wikipedia.org/wiki/Khmer_language)
     * ([khm][LanguageAlpha3Code.khm]).
     */
    km,
    /**
     * [Kannada](http://en.wikipedia.org/wiki/Kannada_language)
     * ([kan][LanguageAlpha3Code.kan]).
     */
    kn,
    /**
     * [Korean](http://en.wikipedia.org/wiki/Korean_language)
     * ([kor][LanguageAlpha3Code.kor]).
     */
    ko,
    /**
     * [Kanuri](http://en.wikipedia.org/wiki/Kanuri_language)
     * ([kau][LanguageAlpha3Code.kau]).
     */
    kr,
    /**
     * [Kashmiri](http://en.wikipedia.org/wiki/Kashmiri_language)
     * ([kas][LanguageAlpha3Code.kas]).
     */
    ks,
    /**
     * [Kurdish](http://en.wikipedia.org/wiki/Kurdish_language)
     * ([kur][LanguageAlpha3Code.kur]).
     */
    ku,
    /**
     * [Komi](http://en.wikipedia.org/wiki/Komi_language)
     * ([kom][LanguageAlpha3Code.kom]).
     */
    kv,
    /**
     * [Cornish](http://en.wikipedia.org/wiki/Cornish_language)
     * ([cor][LanguageAlpha3Code.cor]).
     */
    kw,
    /**
     * [Kyrgyz](http://en.wikipedia.org/wiki/Kyrgyz_language)
     * ([kir][LanguageAlpha3Code.kir]).
     */
    ky,
    /**
     * [Latin](http://en.wikipedia.org/wiki/Latin)
     * ([lat][LanguageAlpha3Code.lat]).
     */
    la,
    /**
     * [Luxembourgish](http://en.wikipedia.org/wiki/Luxembourgish_language)
     * ([ltz][LanguageAlpha3Code.ltz]).
     */
    lb,
    /**
     * [Ganda](http://en.wikipedia.org/wiki/Luganda)
     * ([lug][LanguageAlpha3Code.lug]).
     */
    lg,
    /**
     * [Limburgish](http://en.wikipedia.org/wiki/Limburgish_language)
     * ([lim][LanguageAlpha3Code.lim]).
     */
    li,
    /**
     * [Lingala](http://en.wikipedia.org/wiki/Lingala_language)
     * ([lin][LanguageAlpha3Code.lin]).
     */
    ln,
    /**
     * [Lao](http://en.wikipedia.org/wiki/Lao_language)
     * ([lao][LanguageAlpha3Code.lao]).
     */
    lo,
    /**
     * [Lithuanian](http://en.wikipedia.org/wiki/Lithuanian_language)
     * ([lit][LanguageAlpha3Code.lit]).
     */
    lt,
    /**
     * [Luba-Katanga](http://en.wikipedia.org/wiki/Tshiluba_language)
     * ([lub][LanguageAlpha3Code.lub]).
     */
    lu,
    /**
     * [Latvian](http://en.wikipedia.org/wiki/Latvian_language)
     * ([lav][LanguageAlpha3Code.lav]).
     */
    lv,
    /**
     * [Malagasy](http://en.wikipedia.org/wiki/Malagasy_language)
     * ([mlg][LanguageAlpha3Code.mlg]).
     */
    mg,
    /**
     * [Marshallese](http://en.wikipedia.org/wiki/Marshallese_language)
     * ([mah][LanguageAlpha3Code.mah]).
     */
    mh,
    /**
     * [M&#257;ori](http://en.wikipedia.org/wiki/M%C4%81ori_language)
     * ([mir][LanguageAlpha3Code.mri], [mao][LanguageAlpha3Code.mao]).
     */
    mi,
    /**
     * [Macedonian](http://en.wikipedia.org/wiki/Macedonian_language)
     * ([mkd][LanguageAlpha3Code.mkd], [mac][LanguageAlpha3Code.mac])).
     */
    mk,
    /**
     * [Malayalam](http://en.wikipedia.org/wiki/Malayalam_language)
     * ([mal][LanguageAlpha3Code.mal]).
     */
    ml,
    /**
     * [Mongolian](http://en.wikipedia.org/wiki/Mongolian_language)
     * ([mon][LanguageAlpha3Code.mon]).
     */
    mn,
    /**
     * [Marathi](http://en.wikipedia.org/wiki/Marathi_language)
     * ([mar][LanguageAlpha3Code.mar]).
     */
    mr,
    /**
     * [Malay](http://en.wikipedia.org/wiki/Malay_language)
     * ([msa][LanguageAlpha3Code.msa], [may][LanguageAlpha3Code.may]).
     */
    ms,
    /**
     * [Maltese](http://en.wikipedia.org/wiki/Maltese_language)
     * ([mlt][LanguageAlpha3Code.mlt]).
     */
    mt,
    /**
     * [Burmese](http://en.wikipedia.org/wiki/Burmese_language)
     * ([may][LanguageAlpha3Code.mya], [bur][LanguageAlpha3Code.bur]).
     */
    my,
    /**
     * [Nauru](http://en.wikipedia.org/wiki/Nauruan_language)
     * ([nau][LanguageAlpha3Code.nau]).
     */
    na,
    /**
     * [Norwegian Bokml](http://en.wikipedia.org/wiki/Bokm%C3%A5l)
     * ([nob][LanguageAlpha3Code.nob]).
     */
    nb,
    /**
     * [Northern Ndebele](http://en.wikipedia.org/wiki/Northern_Ndebele_language)
     * ([nde][LanguageAlpha3Code.nde]).
     */
    nd,
    /**
     * [Nepali](http://en.wikipedia.org/wiki/Nepali_language)
     * ([nep][LanguageAlpha3Code.nep]).
     */
    ne,
    /**
     * [Ndonga](http://en.wikipedia.org/wiki/Ndonga)
     * ([ndo][LanguageAlpha3Code.ndo]).
     */
    ng,
    /**
     * [Dutch](http://en.wikipedia.org/wiki/Dutch_language)
     * ([nld][LanguageAlpha3Code.nld], [dut][LanguageAlpha3Code.dut]).
     */
    nl,
    /**
     * [Norwegian Nynorsk](http://en.wikipedia.org/wiki/Nynorsk)
     * ([nno][LanguageAlpha3Code.nno]).
     */
    nn,
    /**
     * [Norwegian](http://en.wikipedia.org/wiki/Norwegian_language)
     * ([nor][LanguageAlpha3Code.nor]).
     *
     * @see [Sprkkoder for POSIX locale i Norge](http://i18n.skolelinux.no/localekoder.txt)
     *
     * @see [Red Hat Bugzilla – Bug 532487 Legacy Norwegian locale
    ](https://bugzilla.redhat.com/show_bug.cgi?id=532487) */
    no,
    /**
     * [Southern Ndebele](http://en.wikipedia.org/wiki/Southern_Ndebele_language)
     * ([nbl][LanguageAlpha3Code.nbl]).
     */
    nr,
    /**
     * [Navajo](http://en.wikipedia.org/wiki/Navajo_language)
     * ([nav][LanguageAlpha3Code.nav]).
     */
    nv,
    /**
     * [Chichewa](http://en.wikipedia.org/wiki/Chichewa_language)
     * ([nya][LanguageAlpha3Code.nya]).
     */
    ny,
    /**
     * [Occitan](http://en.wikipedia.org/wiki/Occitan_language)
     * ([oci][LanguageAlpha3Code.oci]).
     */
    oc,
    /**
     * [Ojibwe](http://en.wikipedia.org/wiki/Ojibwe_language)
     * ([oji][LanguageAlpha3Code.oji]).
     */
    oj,
    /**
     * [Oromo](http://en.wikipedia.org/wiki/Oromo_language)
     * ([orm][LanguageAlpha3Code.orm]).
     */
    om,
    /**
     * [Oriya](http://en.wikipedia.org/wiki/Oriya_language)
     * ([ori][LanguageAlpha3Code.ori]).
     */
    or,
    /**
     * [Ossetian](http://en.wikipedia.org/wiki/Ossetic_language)
     * ([oss][LanguageAlpha3Code.oss]).
     */
    os,
    /**
     * [Punjabi](http://en.wikipedia.org/wiki/Punjabi_language)
     * ([pan][LanguageAlpha3Code.pan]).
     */
    pa,
    /**
     * [P&#257;li](http://en.wikipedia.org/wiki/P%C4%81li_language)
     * ([pli][LanguageAlpha3Code.pli]).
     */
    pi,
    /**
     * [Polish](http://en.wikipedia.org/wiki/Polish_language)
     * ([pol][LanguageAlpha3Code.pol]).
     */
    pl,
    /**
     * [Pashto](http://en.wikipedia.org/wiki/Pashto_language)
     * ([pus][LanguageAlpha3Code.pus]).
     */
    ps,
    /**
     * [Portuguese](http://en.wikipedia.org/wiki/Portuguese_language)
     * ([por][LanguageAlpha3Code.por]).
     */
    pt,
    /**
     * [Quechua](http://en.wikipedia.org/wiki/Quechua_language)
     * ([que][LanguageAlpha3Code.que]).
     */
    qu,
    /**
     * [Romansh](http://en.wikipedia.org/wiki/Romansh_language)
     * ([roh][LanguageAlpha3Code.roh]).
     */
    rm,
    /**
     * [Kirundi](http://en.wikipedia.org/wiki/Kirundi)
     * ([run][LanguageAlpha3Code.run]).
     */
    rn,
    /**
     * [Romanian](http://en.wikipedia.org/wiki/Romanian_language)
     * ([ron][LanguageAlpha3Code.ron], [rum][LanguageAlpha3Code.rum]).
     */
    ro,
    /**
     * [Russian](http://en.wikipedia.org/wiki/Russian_language)
     * ([run][LanguageAlpha3Code.run]).
     */
    ru,
    /**
     * [Kinyarwanda](http://en.wikipedia.org/wiki/Kinyarwanda)
     * ([kin][LanguageAlpha3Code.kin]).
     */
    rw,
    /**
     * [Sanskrit](http://en.wikipedia.org/wiki/Sanskrit)
     * ([san][LanguageAlpha3Code.san]).
     */
    sa,
    /**
     * [Sardinian](http://en.wikipedia.org/wiki/Sardinian_language)
     * ([srd][LanguageAlpha3Code.srd]).
     */
    sc,
    /**
     * [Sindhi](http://en.wikipedia.org/wiki/Sindhi_language)
     * ([snd][LanguageAlpha3Code.snd]).
     */
    sd,
    /**
     * [Northern Sami](http://en.wikipedia.org/wiki/Northern_Sami)
     * ([sme][LanguageAlpha3Code.sme]).
     */
    se,
    /**
     * [Sango](http://en.wikipedia.org/wiki/Sango_language)
     * ([sag][LanguageAlpha3Code.sag]).
     */
    sg,
    /**
     * [Sinhala](http://en.wikipedia.org/wiki/Sinhala_language)
     * ([sin][LanguageAlpha3Code.sin]).
     */
    si,
    /**
     * [Slovak](http://en.wikipedia.org/wiki/Slovak_language)
     * ([slk][LanguageAlpha3Code.slk], [slo][LanguageAlpha3Code.slo]).
     */
    sk,
    /**
     * [Slovene](http://en.wikipedia.org/wiki/Slovene_language)
     * ([slv][LanguageAlpha3Code.slv]).
     */
    sl,
    /**
     * [Samoan](http://en.wikipedia.org/wiki/Samoan_language)
     * ([smo][LanguageAlpha3Code.smo]).
     */
    sm,
    /**
     * [Shona](http://en.wikipedia.org/wiki/Shona_language)
     * ([sna][LanguageAlpha3Code.sna]).
     */
    sn,
    /**
     * [Somali](http://en.wikipedia.org/wiki/Somali_language)
     * ([som][LanguageAlpha3Code.som]).
     */
    so,
    /**
     * [Albanian](http://en.wikipedia.org/wiki/Albanian_language)
     * ([sqi][LanguageAlpha3Code.sqi], [alb][LanguageAlpha3Code.alb]).
     */
    sq,
    /**
     * [Serbian](http://en.wikipedia.org/wiki/Serbian_language)
     * ([srp][LanguageAlpha3Code.srp]).
     */
    sr,
    /**
     * [Swati](http://en.wikipedia.org/wiki/Swati_language)
     * ([ssw][LanguageAlpha3Code.ssw]).
     */
    ss,
    /**
     * [Southern Sotho](http://en.wikipedia.org/wiki/Sotho_language)
     * ([sot][LanguageAlpha3Code.sot]).
     */
    st,
    /**
     * [Sundanese](http://en.wikipedia.org/wiki/Sundanese_language)
     * ([sun][LanguageAlpha3Code.sun]).
     */
    su,
    /**
     * [Swedish](http://en.wikipedia.org/wiki/Swedish_language)
     * ([swe][LanguageAlpha3Code.swe]).
     */
    sv,
    /**
     * [Swahili](http://en.wikipedia.org/wiki/Swahili_language)
     * ([swa][LanguageAlpha3Code.swa]).
     */
    sw,
    /**
     * [Tamil](http://en.wikipedia.org/wiki/Tamil_language)
     * ([tam][LanguageAlpha3Code.tam]).
     */
    ta,
    /**
     * [Telugu](http://en.wikipedia.org/wiki/Telugu_language)
     * ([tel][LanguageAlpha3Code.tel]).
     */
    te,
    /**
     * [Tajik](http://en.wikipedia.org/wiki/Tajik_language)
     * ([tgk][LanguageAlpha3Code.tgk]).
     */
    tg,
    /**
     * [Thai](http://en.wikipedia.org/wiki/Thai_language)
     * ([tha][LanguageAlpha3Code.tha]).
     */
    th,
    /**
     * [Tigrinya](http://en.wikipedia.org/wiki/Tigrinya_language)
     * ([tir][LanguageAlpha3Code.tir]).
     */
    ti,
    /**
     * [Turkmen](http://en.wikipedia.org/wiki/Turkmen_language)
     * ([tuk][LanguageAlpha3Code.tuk]).
     */
    tk,
    /**
     * [Tagalog](http://en.wikipedia.org/wiki/Tagalog_language)
     * ([tgl][LanguageAlpha3Code.tgl]).
     */
    tl,
    /**
     * [Tswana](http://en.wikipedia.org/wiki/Tswana_language)
     * ([tsn][LanguageAlpha3Code.tsn]).
     */
    tn,
    /**
     * [Tongan](http://en.wikipedia.org/wiki/Tongan_language)
     * ([ton][LanguageAlpha3Code.ton]).
     */
    to,
    /**
     * [Turkish](http://en.wikipedia.org/wiki/Turkish_language)
     * ([tur][LanguageAlpha3Code.tur]).
     */
    tr,
    /**
     * [Tsonga](http://en.wikipedia.org/wiki/Tsonga_language)
     * ([tso][LanguageAlpha3Code.tso]).
     */
    ts,
    /**
     * [Tatar](http://en.wikipedia.org/wiki/Tatar_language)
     * ([tat][LanguageAlpha3Code.tat]).
     */
    tt,
    /**
     * [Twi](http://en.wikipedia.org/wiki/Twi)
     * ([twi][LanguageAlpha3Code.twi]).
     */
    tw,
    /**
     * [Tahitian](http://en.wikipedia.org/wiki/Tahitian_language)
     * ([tah][LanguageAlpha3Code.tah]).
     */
    ty,
    /**
     * [Uighur](http://en.wikipedia.org/wiki/Uyghur_language)
     * ([uig][LanguageAlpha3Code.uig]).
     */
    ug,
    /**
     * [Ukrainian](http://en.wikipedia.org/wiki/Ukrainian_language)
     * ([ukr][LanguageAlpha3Code.ukr]).
     */
    uk,
    /**
     * [Urdu](http://en.wikipedia.org/wiki/Urdu)
     * ([urd][LanguageAlpha3Code.urd]).
     */
    ur,
    /**
     * [Uzbek](http://en.wikipedia.org/wiki/Uzbek_language)
     * ([uzb][LanguageAlpha3Code.uzb]).
     */
    uz,
    /**
     * [Venda](http://en.wikipedia.org/wiki/Venda_language)
     * ([ven][LanguageAlpha3Code.ven]).
     */
    ve,
    /**
     * [Vietnamese](http://en.wikipedia.org/wiki/Vietnamese_language)
     * ([vie][LanguageAlpha3Code.vie]).
     */
    vi,
    /**
     * [Volapk](http://en.wikipedia.org/wiki/Volap%C3%BCk)
     * ([vol][LanguageAlpha3Code.vol]).
     */
    vo,
    /**
     * [Walloon](http://en.wikipedia.org/wiki/Walloon_language)
     * ([wln][LanguageAlpha3Code.wln]).
     */
    wa,
    /**
     * [Wolof](http://en.wikipedia.org/wiki/Wolof_language)
     * ([wol][LanguageAlpha3Code.wol]).
     */
    wo,
    /**
     * [Xhosa](http://en.wikipedia.org/wiki/Xhosa_language)
     * ([xho][LanguageAlpha3Code.xho]).
     */
    xh,
    /**
     * [Yiddish](http://en.wikipedia.org/wiki/Yiddish_language)
     * ([yid][LanguageAlpha3Code.yid]).
     */
    yi,
    /**
     * [Yoruba](http://en.wikipedia.org/wiki/Yoruba_language)
     * ([yor][LanguageAlpha3Code.yor]).
     */
    yo,
    /**
     * [Zhuang](http://en.wikipedia.org/wiki/Zhuang_languages)
     * ([zha][LanguageAlpha3Code.zha]).
     */
    za,
    /**
     * [Chinese](http://en.wikipedia.org/wiki/Chinese_language)
     * ([zho][LanguageAlpha3Code.zho], [chi][LanguageAlpha3Code.chi]).
     */
    zh,
}