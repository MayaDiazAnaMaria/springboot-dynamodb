resource "aws_cloudwatch_log_group" "ecs_log_group" {
  name              = "/ecs/springboot"
  retention_in_days = 7

  tags = {
    Name = "springboot-logs"
  }
}
