rootProject.name = "microservice-tracing"

include(
    "api",

    ":cloud-services:config-server",
    ":cloud-services:eureka-server",
    ":cloud-services:gateway",

    ":microservices:product-composite-service",
    ":microservices:product-service",
    ":microservices:review-service"
)
