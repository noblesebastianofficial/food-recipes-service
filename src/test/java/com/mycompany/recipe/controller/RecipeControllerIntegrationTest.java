package com.mycompany.recipe.controller;

import static org.springframework.http.MediaType.APPLICATION_JSON;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;


import com.mycompany.recipe.BaseIntegrationIT;
import com.mycompany.recipe.dto.IngredientDto;
import com.mycompany.recipe.dto.RecipeDto;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.Test;



class RecipeControllerIntegrationTest extends BaseIntegrationIT {
    private static final String API_ROOT = "/api/recipes";

    @WithMockUser(username="admin")
    @Test
    void getAllRecipes_findRecipes_isSuccessful() throws Exception{
        mockMvc.perform(get(API_ROOT )
            .accept(APPLICATION_JSON))
            .andExpect(status().isOk());
    }


    @Test
    void getAllRecipes_findRecipes_isUnauthorized() throws Exception{
        mockMvc.perform(get(API_ROOT )
            .accept(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin")
    @Test
    void getAllRecipes_findRecipes_isNotFound() throws Exception{
        mockMvc.perform(get(API_ROOT+"bash" )
            .accept(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @WithMockUser(username="admin")
    @Test
    void givenRecipeId_getRecipeById_isNotFound() throws Exception{
        mockMvc.perform(get(API_ROOT+"/10" )
            .accept(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }


    @Test
    void givenRecipeId_getRecipeById_isUnauthorized() throws Exception{
        mockMvc.perform(get(API_ROOT+"/1" )
            .accept(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin")
    @Test
    void givenRecipeId_getRecipeById_isSuccessful() throws Exception{
        // Given
        RecipeDto recipeResponseDto = createRecipeDto();
        // When
        mockMvc.perform(get(API_ROOT+"/"+recipeResponseDto.getId() )
            .accept(APPLICATION_JSON))
        //Then
            .andExpect(status().isOk());
    }


    @WithMockUser(username="admin")
    @Test
    void givenRecipeId_deleteRecipe_isNotFound() throws Exception{
        mockMvc.perform(delete(API_ROOT+"/150" )
            .accept(APPLICATION_JSON))
            .andExpect(status().isNotFound());
    }

    @Test
    void givenRecipeId_deleteRecipe_isUnauthorized() throws Exception{
        mockMvc.perform(delete(API_ROOT+"/1" )
            .accept(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin")
    @Test
    void givenRecipeId_deleteRecipe_isOk() throws Exception{
        // Given
        RecipeDto recipeResponseDto = createRecipeDto();
        // When
        mockMvc.perform(delete(API_ROOT+"/"+recipeResponseDto.getId() )
            .accept(APPLICATION_JSON))
        //Then
            .andExpect(status().isOk());
    }

    @WithMockUser(username="admin")
    @Test
    void givenCreateRecipe_createRecipe_isBadRequest() throws Exception{
        mockMvc.perform(post(API_ROOT )
            .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }

    @Test
    void givenCreateRecipe_createRecipe_isUnauthorized() throws Exception{
        mockMvc.perform(post(API_ROOT )
            .accept(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin")
    @Test
    void givenCreateRecipe_createRecipe_isCreated() throws Exception{
        // Given
        RecipeDto recipeDto = getRecipeDto();
        // When
        mockMvc.perform(post(API_ROOT )
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(recipeDto)))
        //Then
            .andExpect(status().isCreated());
    }


    @Test
    void givenUpdateRecipe_updateRecipe_isUnauthorized() throws Exception{
        mockMvc.perform(put(API_ROOT )
            .accept(APPLICATION_JSON))
            .andExpect(status().isUnauthorized());
    }

    @WithMockUser(username="admin")
    @Test
    void givenUpdateRecipe_updateRecipe_isBadRequest() throws Exception{
        mockMvc.perform(put(API_ROOT )
            .accept(APPLICATION_JSON))
            .andExpect(status().isBadRequest());
    }


    @WithMockUser(username="admin")
    @Test
    void givenUpdateRecipe_updateRecipe_isOk() throws Exception{
        // Given
        RecipeDto recipeResponseDto = createRecipeDto();
        recipeResponseDto.setIsVeg(false);
        // When
        mockMvc.perform(put(API_ROOT )
            .contentType(APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(recipeResponseDto)))
        //Then
            .andExpect(status().isOk());
    }

    private RecipeDto createRecipeDto() {
        RecipeDto recipeDto = getRecipeDto();
        return recipeService.createRecipe(recipeDto);
    }

    private RecipeDto getRecipeDto() {
        List<IngredientDto> ingredientDtoList= new ArrayList<>();
        ingredientDtoList.add(IngredientDto.builder().amount(new BigDecimal("5.00")).description("beef-stuck").build());

        return RecipeDto.builder().name("Pot-roast beef").isVeg(false).servings("2").
            cookingInstructions("marinated beef").ingredients(ingredientDtoList).build();
    }


}
