variable "aws_region" {
  default = "us-east-1"
}


variable "read_capacity" {
  description = "Read capacity units"
  type        = number
  default     = 5
}

variable "write_capacity" {
  description = "Write capacity units"
  type        = number
  default     = 5
}

variable "s3_bucket_name" {
  description = "Nombre del bucket S3 para el backend"
  type        = string
}

variable "aws_profile" {
  description = "AWS CLI profile a usar"
  default     = "default"
}

variable "ecr_repo_name" {
  description = "Nombre del repositorio ECR"
  default     = "springboot-dynamodb"
}

variable "ecs_cluster_name" {
  description = "Nombre del cluster ECS"
  default     = "springboot-cluster"
}

variable "ecs_cpu" {
  description = "CPU units para ECS task"
  default     = "512"
}

variable "ecs_memory" {
  description = "Memoria para ECS task"
  default     = "1024"
}

variable "ecs_task_count" {
  description = "Número de tareas deseadas en ECS"
  default     = 1
}

variable "api_stage_name" {
  description = "Nombre del stage para API Gateway"
  default     = "prod"
}
