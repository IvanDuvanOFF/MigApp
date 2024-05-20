import 'package:deeplom/data/dto/notification_dto.dart';
import 'package:deeplom/data/models/notification_model.dart';

extension NotificationDtoExt on NotificationDto {
  NotificationModel toModel() {
    return NotificationModel(
      notificationId: int.parse(notificationId ?? ''),
      userId: userId ?? '',
      title: title ?? '',
      description: description ?? '',
      date: DateTime.parse(date ?? ''),
      isViewed: isViewed ?? false,
      status: status ?? '',
    );
  }
}
