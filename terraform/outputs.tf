output "alb_dns_name" {
  description = "DNS público del Application Load Balancer"
  value       = aws_lb.app_lb.dns_name
}

output "alb_url" {
  description = "URL HTTP completa del ALB"
  value       = "http://${aws_lb.app_lb.dns_name}"
}

output "api_gateway_url" {
  description = "Endpoint del API Gateway HTTP API"
  value       = aws_apigatewayv2_stage.default_stage.invoke_url
}

output "ecs_cluster_name" {
  description = "Nombre del ECS Cluster"
  value       = aws_ecs_cluster.app_cluster.name
}

output "ecs_service_name" {
  description = "Nombre del ECS Service"
  value       = aws_ecs_service.app_service.name
}

output "ecr_repository_url" {
  description = "URL del repositorio ECR"
  value       = aws_ecr_repository.app_repo.repository_url
}

output "target_group_arn" {
  description = "ARN del Target Group del ALB"
  value       = aws_lb_target_group.app_tg.arn
}
