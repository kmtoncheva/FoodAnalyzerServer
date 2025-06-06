package data;

import bg.sofia.uni.fmi.mjt.server.dto.model.FoodItemDto;

public final class FoodData {
    public final static String brandedFoodItem = """
        {
          "discontinuedDate": "",
          "foodComponents": [],
          "foodAttributes": [],
          "foodPortions": [],
          "fdcId": 534358,
          "description": "NUT 'N BERRY MIX",
          "publicationDate": "4/1/2019",
          "foodNutrients": [
            {
              "type": "FoodNutrient",
              "nutrient": {
                "id": 1005,
                "number": "205",
                "name": "Carbohydrate, by difference",
                "rank": 1110,
                "unitName": "g"
              },
              "foodNutrientDerivation": {
                "id": 70,
                "code": "LCCS",
                "description": "Calculated from value per serving size measure"
              },
              "id": 6000021,
              "amount": 42.86
            }
          ],
          "dataType": "Branded",
          "foodClass": "Branded",
          "modifiedDate": "8/18/2018",
          "availableDate": "8/18/2018",
          "brandOwner": "Kar Nut Products Company",
          "dataSource": "LI",
          "brandedFoodCategory": "Popcorn, Peanuts, Seeds & Related Snacks",
          "gtinUpc": "077034085228",
          "householdServingFullText": "1 ONZ",
          "ingredients": "PEANUTS (PEANUTS, PEANUT AND/OR SUNFLOWER OIL). RAISINS. DRIED CRANBERRIES (CRANBERRIES, SUGAR, SUNFLOWER OIL). SUNFLOWER KERNELS AND ALMONDS (SUNFLOWER KERNELS AND ALMONDS, PEANUT AND/OR SUNFLOWER OIL).",
          "marketCountry": "United States",
          "servingSize": 28,
          "servingSizeUnit": "g",
          "foodUpdateLog": [
            {
              "discontinuedDate": "",
              "foodAttributes": [],
              "fdcId": 534358,
              "description": "NUT 'N BERRY MIX",
              "publicationDate": "4/1/2019",
              "dataType": "Branded",
              "foodClass": "Branded",
              "modifiedDate": "8/18/2018",
              "availableDate": "8/18/2018",
              "brandOwner": "Kar Nut Products Company",
              "dataSource": "LI",
              "brandedFoodCategory": "Popcorn, Peanuts, Seeds & Related Snacks",
              "gtinUpc": "077034085228",
              "householdServingFullText": "1 ONZ",
              "ingredients": "PEANUTS (PEANUTS, PEANUT AND/OR SUNFLOWER OIL). RAISINS. DRIED CRANBERRIES (CRANBERRIES, SUGAR, SUNFLOWER OIL). SUNFLOWER KERNELS AND ALMONDS (SUNFLOWER KERNELS AND ALMONDS, PEANUT AND/OR SUNFLOWER OIL).",
              "marketCountry": "United States",
              "servingSize": 28,
              "servingSizeUnit": "g"
            }
          ],
          "labelNutrients": {
            "fat": {
              "value": 9
            },
            "sodium": {
              "value": 0
            },
            "fiber": {
              "value": 1.99
            },
            "sugars": {
              "value": 8
            },
            "protein": {
              "value": 4
            },
            "calories": {
              "value": 140
            }
          }
        }
        """;

    public final static String foundationFoodItem = """  
        {
          "fdcId": 2262074,
          "description": "Almond butter, creamy",
          "publicationDate": "4/28/2022",
          "foodNutrients": [
            {
              "type": "FoodNutrient",
              "nutrient": {
                "id": 1003,
                "number": "203",
                "name": "Protein",
                "rank": 600,
                "unitName": "g"
              },
              "foodNutrientDerivation": {
                "id": 49,
                "code": "NC",
                "description": "Calculated",
                "foodNutrientSource": {
                  "id": 2,
                  "code": "4",
                  "description": "Calculated or imputed"
                }
              },
              "id": 27795967,
              "amount": 20.78734,
              "max": 22.533,
              "min": 15.6436,
              "median": 21.2898,
              "nutrientAnalysisDetails": []
            },
            {
              "type": "FoodNutrient",
              "nutrient": {
                "id": 1005,
                "number": "205",
                "name": "Carbohydrate, by difference",
                "rank": 1110,
                "unitName": "g"
              },
              "foodNutrientDerivation": {
                "id": 49,
                "code": "NC",
                "description": "Calculated",
                "foodNutrientSource": {
                  "id": 2,
                  "code": "4",
                  "description": "Calculated or imputed"
                }
              },
              "id": 27795968,
              "amount": 21.23666,
              "nutrientAnalysisDetails": []
            }
          ],
          "dataType": "Foundation",
          "foodClass": "FinalFood",
          "inputFoods": [
            {
              "id": 104029,
              "foodDescription": "almond butter, creamy",
              "inputFood": {
                "fdcId": 2262103,
                "description": "almond butter, creamy",
                "publicationDate": "4/28/2022",
                "foodAttributeTypes": [],
                "foodClass": "Composite",
                "totalRefuse": 0,
                "dataType": "Sample",
                "foodGroup": {
                  "id": 12,
                  "code": "1200",
                  "description": "Nut and Seed Products"
                }
              }
            }
          ],
          "foodComponents": [],
          "foodAttributes": [],
          "nutrientConversionFactors": [
            {
              "id": 22888,
              "proteinValue": 3.47,
              "fatValue": 8.37,
              "carbohydrateValue": 4.07,
              "type": ".CalorieConversionFactor",
              "name": "Calories From Proximates"
            },
            {
              "id": 22892,
              "value": 5.18,
              "type": ".ProteinConversionFactor",
              "name": "Protein From Nitrogen"
            }
          ],
          "ndbNumber": 12195,
          "isHistoricalReference": false,
          "foodCategory": {
            "id": 12,
            "code": "1200",
            "description": "Nut and Seed Products"
          }
        }
        """;

    public final static String srLegacyFoodItem = """  
        {
          "fdcId": 167782,
          "description": "Abiyuch, raw",
          "publicationDate": "4/1/2019",
          "foodNutrients": [
            {
              "type": "FoodNutrient",
              "nutrient": {
                "id": 1003,
                "number": "203",
                "name": "Protein",
                "rank": 600,
                "unitName": "g"
              },
              "foodNutrientDerivation": {
                "id": 1,
                "code": "A",
                "description": "Analytical",
                "foodNutrientSource": {
                  "id": 1,
                  "code": "1",
                  "description": "Analytical or derived from analytical"
                }
              },
              "id": 1304515,
              "amount": 1.5,
              "dataPoints": 1
            },
            {
              "type": "FoodNutrient",
              "nutrient": {
                "id": 1004,
                "number": "204",
                "name": "Total lipid (fat)",
                "rank": 800,
                "unitName": "g"
              },
              "foodNutrientDerivation": {
                "id": 1,
                "code": "A",
                "description": "Analytical",
                "foodNutrientSource": {
                  "id": 1,
                  "code": "1",
                  "description": "Analytical or derived from analytical"
                }
              },
              "id": 1304514,
              "amount": 0.1,
              "dataPoints": 1
            },
            {
              "type": "FoodNutrient",
              "nutrient": {
                "id": 1005,
                "number": "205",
                "name": "Carbohydrate, by difference",
                "rank": 1110,
                "unitName": "g"
              },
              "foodNutrientDerivation": {
                "id": 49,
                "code": "NC",
                "description": "Calculated",
                "foodNutrientSource": {
                  "id": 2,
                  "code": "4",
                  "description": "Calculated or imputed"
                }
              },
              "id": 1304502,
              "amount": 17.6,
              "dataPoints": 0
            }
          ],
          "foodPortions": [
            {
              "id": 81972,
              "gramWeight": 114,
              "sequenceNumber": 1,
              "amount": 0.5,
              "modifier": "cup",
              "measureUnit": {
                "id": 9999,
                "name": "undetermined",
                "abbreviation": "undetermined"
              }
            }
          ],
          "dataType": "SR Legacy",
          "foodClass": "FinalFood",
          "scientificName": "Crateva religiosa",
          "foodComponents": [],
          "foodAttributes": [
            {
              "id": 2091,
              "value": "garlic pear",
              "sequenceNumber": 0,
              "foodAttributeType": {
                "id": 1000,
                "name": "Common Name",
                "description": "Common names associated with a food."
              }
            }
          ],
          "nutrientConversionFactors": [
            {
              "id": 11762,
              "proteinValue": 3.36,
              "fatValue": 8.37,
              "carbohydrateValue": 3.6,
              "type": ".CalorieConversionFactor",
              "name": "Calories From Proximates"
            },
            {
              "id": 16533,
              "value": 6.25,
              "type": ".ProteinConversionFactor",
              "name": "Protein From Nitrogen"
            }
          ],
          "inputFoods": [],
          "ndbNumber": 9427,
          "isHistoricalReference": true,
          "foodCategory": {
            "id": 9,
            "code": "0900",
            "description": "Fruits and Fruit Juices"
          }
        }
        """;

    public static final String experimentalFoodItem = """
        {
          "fdcId": 2731074,
          "name": "USDA, FDA, and ODS-NIH Database for the Iodine Content of Common Foods (Release 4.0)",
          "publicationDate": "4/24/2025",
          "dataType": "Experimental",
          "foodClass": "Experimental",
          "abstract": "Iodine is important for thyroid function in human growth, neurologic development, reproduction, and energy metabolism, with deficiency evidenced by goiter and cretinism. While overall U.S. iodine intake is considered sufficient, women, young adults, and non-Hispanic blacks are at high risk for deficiency. Excessive intake can cause thyroid disease or dysfunction. Dietary sources include seaweed, fish and other seafood, dairy, iodized salt, eggs, and dietary supplements. Intake estimates require data on iodine content and variability for foods, while variable concentrations in individual foods make assessments challenging. Varying levels in foods are due to factors such as iodine in soil, supplementation to animals, iodophor use, and iodine-containing ingredients in processed foods. To provide iodine content and variability data in common foods for estimating intake, this database was developed through collaboration among the Methods and Application of Food Composition Laboratory (USDA MAFCL), the Food and Drug Administration (FDA), and the National Institutes of Health Office of Dietary Supplements (NIH-ODS). Historically, USDA analyzed iodine in selected nationwide food and dietary supplement samples. Many of the USDA samples in this database came from studies conducted in 2016 or later. FDA data in this database are from food and beverage samples in FDA's Total Diet Study (TDS) from 2016 or later. FDA and USDA source(s) are indicated in the table. The USDA and FDA foods were analyzed for iodine using inductively coupled plasma mass spectrometry (ICP-MS). Quality controls included standard reference materials (SRMs) and secondary in-house controls. All analytical data were examined at USDA or FDA depending on the source of data. Final values were accepted only after scientific consensus. The dataset in FDC’s Experimental Foods provide iodine content values reported in micrograms per 100 grams for a total of x biological samples representing x foods. This dataset was designed to accompany Release 4.0 of the “USDA, FDA and ODS-NIH Database for the Iodine Content of Common Foods,” which provides food descriptions along with sample sizes, means, standard deviations (when n ≥ 3), and value ranges reported as per serving and as per 100 grams for 478 foods with supporting documentation. Additional data expanding on Release 4.0 will be added in future FDC releases.",
          "attachment": "FoodData_Central_Iodine_Database_tsv_2025-04-24.zip",
          "title": "USDA, FDA, and ODS-NIH Database for the Iodine Content of Common Foods (Release 4.0)",
          "doiNumber": "https://www.ars.usda.gov/northeast-area/beltsville-md-bhnrc/beltsville-human-nutrition-research-center/methods-and-application-of-food-composition-laboratory/mafcl-site-pages/iodine/",
          "doiUrl": "https://www.ars.usda.gov/northeast-area/beltsville-md-bhnrc/beltsville-human-nutrition-research-center/methods-and-application-of-food-composition-laboratory/mafcl-site-pages/iodine/",
          "foodNutrients": []
        }
        """;

    public static final String surveyFoodItem = """
        {
          "foodClass": "Survey",
          "description": "Alfredo sauce with poultry",
          "foodNutrients": [
            {
              "type": "FoodNutrient",
              "id": 34164044,
              "nutrient": {
                "id": 1003,
                "number": "203",
                "name": "Protein",
                "rank": 600,
                "unitName": "g"
              },
              "amount": 6.87
            },
            {
              "type": "FoodNutrient",
              "id": 34164105,
              "nutrient": {
                "id": 1279,
                "number": "630",
                "name": "MUFA 22:1",
                "rank": 12500,
                "unitName": "g"
              },
              "amount": 0.001
            },
            {
              "type": "FoodNutrient",
              "id": 34164106,
              "nutrient": {
                "id": 1280,
                "number": "631",
                "name": "PUFA 22:5 n-3 (DPA)",
                "rank": 15200,
                "unitName": "g"
              },
              "amount": 0.008
            }
          ],
          "foodAttributes": [
            {
              "id": 3319253,
              "value": "Moisture change: 0%",
              "foodAttributeType": {
                "id": 1002,
                "name": "Adjustments",
                "description": "Adjustments made to foods, including moisture changes"
              }
            }
          ],
          "foodCode": "14650180",
          "startDate": "1/1/2021",
          "endDate": "12/31/2023",
          "wweiaFoodCategory": {
            "wweiaFoodCategoryDescription": "Dips, gravies, other sauces",
            "wweiaFoodCategoryCode": 3299174
          },
          "fdcId": 2705813,
          "dataType": "Survey (FNDDS)",
          "foodPortions": [
            {
              "id": 292027,
              "measureUnit": {
                "id": 9999,
                "name": "undetermined",
                "abbreviation": "undetermined"
              },
              "modifier": "90000",
              "gramWeight": 65,
              "portionDescription": "Quantity not specified",
              "sequenceNumber": 2
            }
          ],
          "publicationDate": "10/31/2024",
          "inputFoods": [
            {
              "id": 125197,
              "unit": "GM",
              "portionDescription": "NONE",
              "sequenceNumber": 1
            }
          ]
        }
        """;

    private FoodData() {}
}
