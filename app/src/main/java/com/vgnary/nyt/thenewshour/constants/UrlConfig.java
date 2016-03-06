package com.vgnary.nyt.thenewshour.constants;


public class UrlConfig {
    public static final String DOMAIN = "http://api.nytimes.com";
    public static final String URL_MOST_POPULAR_DATA = DOMAIN + "/svc/search/v2/articlesearch.json?q=new+york+times&page=2&sort=oldest&api-key=f24f0fa1ef7bf9696638d05b7b7e6e2e:11:73777653&offset=5";
    public static final String URL_GEOGRAPHIC_DATA = DOMAIN + "/svc/semantic/v2/geocodes/query.json?feature_class=A&api-key=sample-key";
    public static final String URL_MOVIE_REVIEW = DOMAIN + "/svc/movies/v2/reviews/search.json?api-key=88a36587bd5521934fc6a07d06338e2a:4:73777653";
    public static final String BASE_URL = "nytimes";
    public static final String ACRA_URL = "http://92.168.113.42/report.php";
    public static final String URL_BEST_SELLER_DATA = DOMAIN + "/svc/books/v3/lists/names.json?api-key=sample-key";

}
