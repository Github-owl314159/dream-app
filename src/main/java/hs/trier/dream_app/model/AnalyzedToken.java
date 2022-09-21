package hs.trier.dream_app.model;

public class AnalyzedToken
{
    private String token;
    private String lemma;

    // for future use with WordNet
    private String      foundAntonym;   // "ist Gegenteil von", z.B. gut -> böse
    private String[]    foundSynonyms;  // "ist gleichbedeutend mit", z.B. Orange -> Apfelsine
    private String      foundHolonym;   // "ist Ganzes von", z.B. Haus -> Dach
    private String[]    foundMeronyms;  // "ist Teil von", z.B. Dach -> Haus
    private String      foundHypernym;  // "ist Oberbegriff von", z.B. Säugetier -> Hund
    private String[]    foundHyponyms;  // "ist Unterbegriff von", z.B. Hund -> Säugetier


    public AnalyzedToken(String token)
    {
        this.token = token;
    }

    public String getToken()
    {
        return token;
    }

    public void setToken(String token)
    {
        this.token = token;
    }

    public String getLemma()
    {
        return lemma;
    }

    public void setLemma(String lemma)
    {
        this.lemma = lemma;
    }

    public String getFoundAntonym()
    {
        return foundAntonym;
    }

    public void setFoundAntonym(String foundAntonym)
    {
        this.foundAntonym = foundAntonym;
    }

    public String[] getFoundSynonyms()
    {
        return foundSynonyms;
    }

    public void setFoundSynonyms(String[] foundSynonyms)
    {
        this.foundSynonyms = foundSynonyms;
    }

    public String getFoundHolonym()
    {
        return foundHolonym;
    }

    public void setFoundHolonym(String foundHolonym)
    {
        this.foundHolonym = foundHolonym;
    }

    public String[] getFoundMeronyms()
    {
        return foundMeronyms;
    }

    public void setFoundMeronyms(String[] foundMeronyms)
    {
        this.foundMeronyms = foundMeronyms;
    }

    public String getFoundHypernym()
    {
        return foundHypernym;
    }

    public void setFoundHypernym(String foundHypernym)
    {
        this.foundHypernym = foundHypernym;
    }

    public String[] getFoundHyponyms()
    {
        return foundHyponyms;
    }

    public void setFoundHyponyms(String[] foundHyponyms)
    {
        this.foundHyponyms = foundHyponyms;
    }
}

