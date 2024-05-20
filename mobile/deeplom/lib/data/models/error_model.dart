class ErrorModel {
  final StatusModel status;
  final String message;

  const ErrorModel({
    required this.status,
    required this.message,
  });
}

class StatusModel {
  final int code;
  final String name;

  const StatusModel({
    required this.code,
    required this.name,
  });
}
