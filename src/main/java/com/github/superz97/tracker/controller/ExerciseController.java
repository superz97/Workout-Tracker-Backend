package com.github.superz97.tracker.controller;

import com.github.superz97.tracker.dto.response.ApiResponse;
import com.github.superz97.tracker.dto.response.ExerciseResponse;
import com.github.superz97.tracker.entity.model.ExerciseCategory;
import com.github.superz97.tracker.entity.model.MuscleGroup;
import com.github.superz97.tracker.service.ExerciseService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/exercises")
@RequiredArgsConstructor
@Tag(name = "Exercises", description = "Exercise management APIs")
@SecurityRequirement(name = "Bearer Authentication")
public class ExerciseController {

    private final ExerciseService exerciseService;

    @GetMapping
    @Operation(summary = "Get all exercises", description = "Get all exercises with optional filters")
    public ResponseEntity<ApiResponse<Page<ExerciseResponse>>> getAllExercises(
            @Parameter(description = "Filter by category")
            @RequestParam(required = false) ExerciseCategory category,
            @Parameter(description = "Filter by muscle group")
            @RequestParam(required = false) MuscleGroup muscleGroup,
            @Parameter(description = "Search term for exercise name")
            @RequestParam(required = false) String searchTerm,
            @PageableDefault(size = 20, sort = "name", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<ExerciseResponse> exercises = exerciseService.getAllExercises(category, muscleGroup, searchTerm, pageable);
        return ResponseEntity.ok(ApiResponse.success(exercises));
    }

    @GetMapping("/{id}")
    @Operation(summary = "Get exercise by ID", description = "Get details of a specific exercise")
    public ResponseEntity<ApiResponse<ExerciseResponse>> getExerciseById(@PathVariable Long id) {
        ExerciseResponse exercise = exerciseService.getExerciseById(id);
        return ResponseEntity.ok(ApiResponse.success(exercise));
    }

    @GetMapping("/categories")
    @Operation(summary = "Get all exercise categories", description = "Get list of all available exercise categories")
    public ResponseEntity<ApiResponse<List<String>>> getAllCategories() {
        List<String> categories = exerciseService.getAllCategories();
        return ResponseEntity.ok(ApiResponse.success(categories));
    }

    @GetMapping("/muscle-groups")
    @Operation(summary = "Get all muscle groups", description = "Get list of all available muscle groups")
    public ResponseEntity<ApiResponse<List<String>>> getAllMuscleGroups() {
        List<String> muscleGroups = exerciseService.getAllMuscleGroups();
        return ResponseEntity.ok(ApiResponse.success(muscleGroups));
    }

}
