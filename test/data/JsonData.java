package data;

public final class JsonData {
    public final static String sampleJsonResponse = """
        {
          "foodSearchCriteria": {
            "query": "raffaello treat",
            "generalSearchInput": "raffaello treat",
            "pageNumber": 1,
            "requireAllWords": true
          },
          "totalHits": 1,
          "currentPage": 1,
          "totalPages": 1,
          "foods": [
            {
              "fdcId": 415269,
              "description": "RAFFAELLO, ALMOND COCONUT TREAT",
              "dataType": "Branded",
              "gtinUpc": "009800146130",
              "publishedDate": "2019-04-01",
              "brandOwner": "Ferrero U.S.A., Incorporated",
              "ingredients": "VEGETABLE OILS (PALM AND SHEANUT). DRY COCONUT, SUGAR, ALMONDS, SKIM MILK POWDER, WHEY POWDER (MILK), WHEAT FLOUR, NATURAL AND ARTIFICIAL FLAVORS, LECITHIN AS EMULSIFIER (SOY), SALT, SODIUM BICARBONATE AS LEAVENING AGENT.",
              "allHighlightFields": "",
              "score": 247.10071
            }
          ]
        }
        """;

    public final static String emptyJsonResponse = """
        {
          "totalHits": 0,
          "currentPage": 1,
          "totalPages": 0,
          "pageList": [],
          "foodSearchCriteria": {
            "dataType": [
              "Foundation"
            ],
            "query": "ghtr56554g",
            "generalSearchInput": "ghtr56554g",
            "pageNumber": 1,
            "sortOrder": "asc",
            "numberOfResultsPerPage": 50,
            "pageSize": 50,
            "requireAllWords": false,
            "foodTypes": [
              "Foundation"
            ]
          },
          "foods": [],
          "aggregations": {
            "dataType": {},
            "nutrients": {}
          }
        }
        """;

    private JsonData() {}
}
