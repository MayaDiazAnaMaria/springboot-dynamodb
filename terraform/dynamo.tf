resource "aws_dynamodb_table" "franquicia" {
  name         = "franquicia"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "id_franquicia"

  attribute {
    name = "id_franquicia"
    type = "N"
  }
}

resource "aws_dynamodb_table" "counters" {
  name         = "counters"
  billing_mode = "PAY_PER_REQUEST"
  hash_key     = "entity"

  attribute {
    name = "entity"
    type = "S"
  }
}
