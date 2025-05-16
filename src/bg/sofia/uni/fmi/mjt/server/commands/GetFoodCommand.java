package bg.sofia.uni.fmi.mjt.server.commands;

import bg.sofia.uni.fmi.mjt.server.exceptions.api.ApiException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.FoodItemNotFoundException;
import bg.sofia.uni.fmi.mjt.server.exceptions.api.MalformedRequestBodyException;
import bg.sofia.uni.fmi.mjt.server.model.dto.GetFoodDto;
import bg.sofia.uni.fmi.mjt.server.service.FoodService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;

import static bg.sofia.uni.fmi.mjt.server.constants.HttpConstants.DELIMITER;

public class GetFoodCommand implements Command{
    private final String[] tokens; // can be more than one : beef noodles ???

    private final FoodService foodService;

    public GetFoodCommand(String[] tokens, FoodService foodService) {
        this.tokens = tokens;
        this.foodService = foodService;
    }

    @Override
    public String execute() throws ApiException {
        try {
            String response = foodService.searchFood(tokens);

            ObjectMapper mapper = new ObjectMapper();
            GetFoodDto foods = mapper.readValue(response, GetFoodDto.class);

            if(foods.foods().size() == 0) {
                throw new FoodItemNotFoundException(null,
                    "No food items matched your search query: " + String.join(DELIMITER, tokens));
            }

            Object json = mapper.readValue(response, Object.class);

            ObjectWriter writer = mapper.writerWithDefaultPrettyPrinter();
            String prettyJson = writer.writeValueAsString(json);
           // System.out.println(foods.getFoods().size());
           // System.out.println(prettyJson);

            return mapper.writeValueAsString(foods);
        } catch (JsonProcessingException e) {
            throw new MalformedRequestBodyException("Invalid JSON payload: " + e.getMessage(),
                "Server unable to process your request. Please try again later.", e);
        }
    }
}