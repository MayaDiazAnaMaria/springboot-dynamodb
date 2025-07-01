resource "aws_apigatewayv2_api" "http_api" {
  name          = "springboot-api"
  protocol_type = "HTTP"

  tags = {
    Name = "springboot-http-api"
  }
}

resource "aws_apigatewayv2_integration" "http_integration" {
  api_id             = aws_apigatewayv2_api.http_api.id
  integration_type   = "HTTP_PROXY"
  integration_method = "ANY"
  integration_uri    = "http://${aws_lb.app_lb.dns_name}"

  timeout_milliseconds = 29000
}

resource "aws_apigatewayv2_route" "proxy_route" {
  api_id    = aws_apigatewayv2_api.http_api.id
  route_key = "ANY /{proxy+}"
  target    = "integrations/${aws_apigatewayv2_integration.http_integration.id}"
}

resource "aws_apigatewayv2_stage" "default_stage" {
  api_id      = aws_apigatewayv2_api.http_api.id
  name        = "$default"   # ⚡ HTTP API usa $default
  auto_deploy = true
}

