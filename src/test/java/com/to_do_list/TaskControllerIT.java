package com.to_do_list;

import com.to_do_list.commons.BaseIT;
import com.to_do_list.dto.TaskDto;
import com.to_do_list.dto.TaskViewDto;
import com.to_do_list.model.Priority;
import io.restassured.http.ContentType;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static com.to_do_list.model.Priority.URGENT;
import static com.to_do_list.model.TaskStatusEnum.*;
import static io.restassured.RestAssured.given;
import static org.hamcrest.Matchers.*;

class TaskControllerIT extends BaseIT {

    @Test
    void shouldGetPendingTasks() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);

        given()
                .when()
                .get("/tasks/pending")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("id", hasItem(equalTo(id.toString())))
                .body("name", hasItem("Passear com a Lua"))
                .body("description", hasItem("Caminhar 40 minutos"))
                .body("priority", hasItem("MODERATE"))
                .body("status", hasItem("CREATED"))
                .body("created", hasItem(notNullValue()))
                .body("started", hasItem(nullValue()))
                .body("done", hasItem(nullValue()))
        ;
    }

    @Test
    void shouldGetAllTasksByPriority() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.URGENT);
        UUID idPassear = createTask(taskDto);

        TaskDto taskDtoUrgent = new TaskDto("Lavar louça", "20 minutos", Priority.URGENT);
        UUID idLavar = createTask(taskDtoUrgent);

        given()
                .when()
                .get("/tasks/priority/{priority}", URGENT)
                .then()
                .statusCode(200)
                .body("$", hasSize(2))
                .body("[0].id", equalTo(idPassear.toString()))
                .body("[0].name", equalTo("Passear com a Lua"))
                .body("[0].description", equalTo("Caminhar 40 minutos"))
                .body("[0].priority", equalTo(URGENT.name()))
                .body("[0].status", equalTo("CREATED"))
                .body("[0].created", notNullValue())
                .body("[0].started", equalTo(null))
                .body("[0].done", equalTo(null))
                .body("[1].id", equalTo(idLavar.toString()))
                .body("[1].name", equalTo("Lavar louça"))
                .body("[1].description", equalTo("20 minutos"))
                .body("[1].priority", equalTo(URGENT.name()))
                .body("[1].status", equalTo(CREATED.name()))
                .body("[1].created", notNullValue())
                .body("[1].started", nullValue())
                .body("[1].done", nullValue())
        ;
    }

    @Test
    void shouldGetAll() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);

        given()
                .when()
                .get("/tasks")
                .then()
                .statusCode(200)
                .body("$", hasSize(1))
                .body("id", hasItem(id.toString()))
                .body("name", hasItem("Passear com a Lua"))
                .body("description", hasItem("Caminhar 40 minutos"))
                .body("priority", hasItem("MODERATE"))
                .body("created", hasItem(notNullValue()))
                .body("status", hasItem("CREATED"))
                .body("started", hasItem(nullValue()))
                .body("done", hasItem(nullValue()))
        ;
    }

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
                .body("started", equalTo(null))
                .body("status", equalTo("CREATED"))
                .body("done", equalTo(null))
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
                .body("name", equalTo(taskDto.name()))
                .body("description", equalTo(taskDto.description()))
                .body("priority", equalTo(taskDto.priority().name()))
                .body("created", notNullValue())
                .body("status", equalTo(CREATED.name()))
                .extract().response().as(TaskViewDto.class).id();
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
                .body("started", equalTo(null))
                .body("status", equalTo("CREATED"))
                .body("done", equalTo(null))
        ;
    }

    @Test
    void shouldGetTaskByIdWithDurationStarted() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        startTask(id);

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
                .body("started", notNullValue())
                .body("status", equalTo("STARTED"))
                .body("done", equalTo(null))
                .body("duration", equalTo(0))
        ;
    }

    @Test
    void shouldGetTaskByIdWithDurationCreated() {
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
                .body("started", equalTo(null))
                .body("status", equalTo("CREATED"))
                .body("done", equalTo(null))
                .body("duration", equalTo(0))
        ;
    }

    @Test
    void shouldGetTaskByIdWithDurationDone() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        completeTask(id);

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
                .body("started", notNullValue())
                .body("done", notNullValue())
                .body("status", equalTo("DONE"))
                .body("duration", equalTo(0))
        ;
    }

    @Test
    void shouldStartTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        given()
                .when()
                .patch("/tasks/{id}/start", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.toString()))
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 40 minutos"))
                .body("priority", equalTo("MODERATE"))
                .body("started", notNullValue())
                .body("status", equalTo("STARTED"))
                .body("created", notNullValue())
                .body("done", equalTo(null))
                .body("duration", equalTo(0))
        ;
    }

    @Test
    void shouldCompleteTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        startTask(id);
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
                .body("started", notNullValue())
                .body("done", notNullValue())
                .body("status", equalTo("DONE"))
                .body("duration", equalTo(0))
        ;
    }

    @Test
    void shouldNotCompleteTask() {
        TaskDto taskDto = new TaskDto("Passear com a Lua", "Caminhar 40 minutos", Priority.MODERATE);
        UUID id = createTask(taskDto);
        completeTask(id);

        given()
                .when()
                .patch("/tasks/{id}", id)
                .then()
                .statusCode(400)
                .body("message", equalTo("Task already completed."))
        ;
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

    private void completeTask(UUID id) {
        startTask(id);
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
                .body("started", notNullValue())
                .body("done", notNullValue())
                .body("status", equalTo(DONE.name()))
                .body("duration", equalTo(0))
        ;
    }

    private void startTask(UUID id) {
        given()
                .when()
                .patch("/tasks/{id}/start", id)
                .then()
                .statusCode(200)
                .body("id", equalTo(id.toString()))
                .body("name", equalTo("Passear com a Lua"))
                .body("description", equalTo("Caminhar 40 minutos"))
                .body("priority", equalTo("MODERATE"))
                .body("started", notNullValue())
                .body("status", equalTo("STARTED"))
                .body("created", notNullValue())
                .body("done", equalTo(null))
                .body("duration", equalTo(0));
    }
}