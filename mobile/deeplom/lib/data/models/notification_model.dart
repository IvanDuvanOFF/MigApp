class NotificationModel {
  final String notificationId;
  final String userId;
  final String title;
  final String description;
  final DateTime date;
  final bool isViewed;
  final String status;

  const NotificationModel({
    required this.notificationId,
    required this.userId,
    required this.title,
    required this.description,
    required this.date,
    required this.isViewed,
    required this.status,
  });
}
