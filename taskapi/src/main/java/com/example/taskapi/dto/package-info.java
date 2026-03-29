package com.example.taskapi.dto;

/**
 * DTOs (Data Transfer Objects) go here.
 *
 * Django equivalent: Serializers in Django REST Framework.
 *
 * DTOs define what data the API accepts (request DTOs) and returns (response DTOs).
 * You'll create these in Phase 3, Day 8.
 *
 * Why not just use the entity directly?
 * Same reason you use serializers in DRF instead of passing model instances:
 * - Control what fields are exposed
 * - Validate input separately from the model
 * - Decouple API shape from database schema
 *
 * TODO (Day 8): Create TaskRequest and TaskResponse DTOs
 */
