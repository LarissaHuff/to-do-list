package com.to_do_list;

import com.to_do_list.commons.BaseIT;
import com.to_do_list.dto.TaskDto;
import com.to_do_list.dto.TaskViewDto;
import com.to_do_list.model.Priority;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.notNullValue;

class TaskControllerIT extends BaseIT {

    @Test
    void shouldCreateTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        given()
                .contentType(ContentType.JSON)
                .body(gson.toJson(taskDto))
                .when()
                .post("/tasks")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 40 minutos"))
                .body("priority", equalTo("MODERATE"))
                .body("created", notNullValue())
        ;
    }

    @Test
    void shouldGetTaskById() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);

        given()
                .when()
                .get("/tasks/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.toString()))
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 40 minutos"))
                .body("priority", equalTo("MODERATE"))
                .body("created", notNullValue())
        ;
    }

    @Test
    void shouldCompleteTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        given()
                .when()
                .patch("/tasks/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.toString()))
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 40 minutos"))
                .body("priority", equalTo("MODERATE"))
                .body("created", notNullValue())
                .body("done", notNullValue());
    }

    @Test
    void shouldUpdateTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        TaskDto updateDto = new TaskDto("Passear com a Lua", "Caminhar 60 minutos", Priority.URGENT);

        given()
                .contentType(ContentType.JSON)
                .body(gson.toJson(updateDto))
                .when()
                .put("/tasks/{id}", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.toString()))
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 60 minutos"))
                .body("priority", equalTo("URGENT"))
                .body("created", notNullValue())
        ;
    }

    @Test
    void shouldDeleteTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);

        given()
                .when()
                .delete("/tasks/{id}", id)
                .then()
                .statusCode(200);

        given()
                .when()
                .get("/tasks/{id}", id)
                .then()
                .statusCode(400)
                .body("message", equalTo("Task not found"))
        ;
    }

    private UUID createTask(TaskDto taskDto) {
        return given()
                .contentType(ContentType.JSON)
                .body(gson.toJson(taskDto))
                .when()
                .post("/tasks")
                .then()
                .statusCode(200)
                .body("id", notNullValue())
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 40 minutos"))
                .body("priority", equalTo("MODERATE"))
                .body("created", notNullValue())
                .extract().response().as(TaskViewDto.class).id();
    }
}