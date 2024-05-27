import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/notification_dto.g.dart';

@JsonSerializable()
class NotificationDto extends Equatable {
  @JsonKey(name: 'notification_id')
  final String? notificationId;
  @JsonKey(name: 'user_id')
  final String? userId;
  final String? title;
  final String? description;
  final String? date;
  @JsonKey(name: 'is_viewed')
  final bool? isViewed;
  final String? status;

  const NotificationDto({
    this.notificationId,
    this.userId,
    this.title,
    this.description,
    this.date,
    this.isViewed,
    this.status,
  });

  factory NotificationDto.fromJson(Map<String, dynamic> json) => _$NotificationDtoFromJson(json);
  Map<String, dynamic> toJson() => _$NotificationDtoToJson(this);

  @override
  List<Object?> get props => [notificationId, userId, title, description, date, isViewed, status];
}
