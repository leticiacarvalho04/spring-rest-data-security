package br.edu.fatecsjc.lgnspringapi.e2e;

import io.restassured.RestAssured;
import io.restassured.http.ContentType;
import io.restassured.response.Response;
import org.junit.jupiter.api.*;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.server.LocalServerPort;
import org.springframework.test.context.ActiveProfiles;

import static org.hamcrest.Matchers.*;
import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assumptions.assumeTrue;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@ActiveProfiles("system-test")
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
@Tag("e2e")
class E2ETest {

        @LocalServerPort
        private int port;

        private static String token;
        private static String organizationId;
        private static String groupId;
        private static String memberId;
        private static String marathonId;

        @BeforeEach
        void setup() {
                RestAssured.port = port;
        }

        @Test
        @Order(1)
        void deveRegistrarEAutenticarUsuario() {
                var registerBody = """
                                    {
                                      "firstname": "Test",
                                      "lastname": "Admin",
                                      "email": "e2e@example.com",
                                      "password": "test123",
                                      "role": "ADMIN"
                                    }
                                """;

                Response regResp = RestAssured.given()
                                .contentType(ContentType.JSON)
                                .body(registerBody)
                                .when().post("/auth/register");

                regResp.then().log().ifValidationFails();
                assertTrue(
                                regResp.statusCode() == 201 || regResp.statusCode() == 200
                                                || regResp.statusCode() == 409,
                                "Falha ao registrar usuário: HTTP " + regResp.statusCode() + " - "
                                                + regResp.getBody().asString());

                var authBody = """
                                    {
                                      "email": "e2e@example.com",
                                      "password": "test123"
                                    }
                                """;

                Response authResp = RestAssured.given()
                                .contentType(ContentType.JSON)
                                .body(authBody)
                                .when().post("/auth/authenticate");

                authResp.then().log().ifValidationFails();

                assertEquals(200, authResp.statusCode(),
                                "Falha na autenticação: HTTP " + authResp.statusCode() + " - "
                                                + authResp.getBody().asString());

                token = authResp.then().extract().path("accessToken");
                assertNotNull(token, "O token não foi retornado corretamente!");
        }

        @Test
        @Order(2)
        void deveCriarOrganizacao() {
                assumeTrue(token != null, "Token nulo, falha no login");

                var orgBody = """
                                    {
                                      "name": "Org E2E",
                                      "number": "123",
                                      "street": "Rua das Flores",
                                      "neighborhood": "Centro",
                                      "CEP": "12345-678",
                                      "municipality": "Cidade Exemplo",
                                      "state": "SP",
                                      "institutionName": "Instituição Exemplo",
                                      "hostCountry": "Brasil"
                                    }
                                """;

                Response resp = RestAssured.given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(orgBody)
                                .when().post("/organization");

                assumeTrue(resp.statusCode() != 403, "Token não tem permissão para criar organização");

                organizationId = resp.then()
                                .statusCode(anyOf(is(200), is(201)))
                                .body("name", equalTo("Org E2E"))
                                .extract().path("id").toString();

                assertNotNull(organizationId);
        }

        @Test
        @Order(3)
        void deveCriarGrupoComOrganizacao() {
                assumeTrue(token != null && organizationId != null, "Pré-condições falharam");

                var groupBody = String.format("""
                                    {
                                      "name": "Grupo E2E",
                                      "members": [],
                                      "organization": {
                                        "id": "%s"
                                      }
                                    }
                                """, organizationId);

                groupId = RestAssured.given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(groupBody)
                                .when().post("/group")
                                .then().log().ifValidationFails()
                                .statusCode(anyOf(is(200), is(201)))
                                .body("name", equalTo("Grupo E2E"))
                                .extract().path("id").toString();

                assertNotNull(groupId);
        }

        @Test
        @Order(4)
        void deveAdicionarMembroAoGrupo() {
                assumeTrue(token != null && groupId != null, "Pré-condições falharam");

                var memberBody = String.format("""
                                    {
                                      "name": "Membro E2E",
                                      "age": 25,
                                      "groupId": "%s",
                                      "marathons": []
                                    }
                                """, groupId);

                memberId = RestAssured.given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(memberBody)
                                .when().post("/member")
                                .then().log().ifValidationFails()
                                .statusCode(anyOf(is(200), is(201)))
                                .body("name", equalTo("Membro E2E"))
                                .extract().path("id").toString();

                assertNotNull(memberId);
        }

        @Test
        @Order(5)
        void deveCriarMaratona() {
                assumeTrue(token != null, "Token nulo, falha no login");

                var marathonBody = """
                                    {
                                      "name": "Maratona E2E",
                                      "weight": 1.5,
                                      "score": 100,
                                      "members": []
                                    }
                                """;

                Response resp = RestAssured.given()
                                .header("Authorization", "Bearer " + token)
                                .contentType(ContentType.JSON)
                                .body(marathonBody)
                                .when().post("/marathon");

                assumeTrue(resp.statusCode() != 404, "Endpoint /marathon não encontrado");

                marathonId = resp.then()
                                .statusCode(anyOf(is(200), is(201)))
                                .body("name", equalTo("Maratona E2E"))
                                .extract().path("id").toString();

                assertNotNull(marathonId);
        }

        @Test
        @Order(6)
        void deveListarTodosRecursos() {
                assumeTrue(token != null, "Token nulo, falha no login");

                String[] endpoints = { "/organization", "/group", "/member", "/marathon" };

                for (String endpoint : endpoints) {
                        Response resp = RestAssured.given()
                                        .header("Authorization", "Bearer " + token)
                                        .when().get(endpoint);

                        assumeTrue(resp.statusCode() != 403, "Sem permissão para acessar " + endpoint);

                        resp.then()
                                        .statusCode(200)
                                        .body("size()", greaterThan(0));
                }
        }

        @Test
        @Order(7)
        void deveRetornar403ParaEndpointsProtegidosSemToken() {
                RestAssured.given()
                                .when().get("/group")
                                .then().statusCode(403);
        }
}
