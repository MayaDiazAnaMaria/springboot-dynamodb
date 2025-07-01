resource "aws_ecs_cluster" "app_cluster" {
  name = "springboot-cluster"
}

resource "aws_ecs_task_definition" "app_task" {
  family                   = "springboot-task"
  network_mode             = "awsvpc"
  requires_compatibilities = ["FARGATE"]
  cpu                      = "256"
  memory                   = "512"
  execution_role_arn       = aws_iam_role.ecs_task_execution_role.arn

  container_definitions = jsonencode([
    {
      name      = "springboot-container"
      image     = "${aws_ecr_repository.app_repo.repository_url}:latest"
      essential = true
      portMappings = [
        {
          containerPort = 8080
          protocol      = "tcp"
        }
      ]
   logConfiguration = {
  logDriver = "awslogs"
  options = {
    awslogs-group         = "/ecs/springboot"
    awslogs-region        = "us-east-1"
    awslogs-stream-prefix = "springboot"
  }
}
    }
  ])
}

resource "aws_ecs_service" "app_service" {
  name            = "springboot-service"
  cluster         = aws_ecs_cluster.app_cluster.id
  task_definition = aws_ecs_task_definition.app_task.arn
  desired_count   = 1
  launch_type     = "FARGATE"
  platform_version = "LATEST"

network_configuration {
  subnets          = [aws_subnet.public_1.id, aws_subnet.public_2.id]
  security_groups  = [aws_security_group.lb_sg.id]
  assign_public_ip = true
}


  load_balancer {
    target_group_arn = aws_lb_target_group.app_tg.arn
    container_name   = "springboot-container"
    container_port   = 8080
  }

  depends_on = [
    aws_lb_listener.app_listener,
    aws_cloudwatch_log_group.ecs_log_group
  ]
}
