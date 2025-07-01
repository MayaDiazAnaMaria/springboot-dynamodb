aws_region = "us-east-1"

read_capacity  = 5
write_capacity = 5
s3_bucket_name  = "mi-terraform-state-bucket-demo-genz"

aws_profile     = "default"

ecr_repo_name   = "springboot-dynamodb"
ecs_cluster_name = "springboot-cluster"

ecs_cpu    = "512"
ecs_memory = "1024"
ecs_task_count = 1

api_stage_name = "prod"
