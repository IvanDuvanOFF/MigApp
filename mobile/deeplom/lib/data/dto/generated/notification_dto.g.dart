// GENERATED CODE - DO NOT MODIFY BY HAND

part of '../notification_dto.dart';

// **************************************************************************
// JsonSerializableGenerator
// **************************************************************************

NotificationDto _$NotificationDtoFromJson(Map<String, dynamic> json) => NotificationDto(
      notificationId: json['notification_id'] as String?,
      userId: json['user_id'] as String?,
      title: json['title'] as String?,
      description: json['description'] as String?,
      date: json['date'] as String?,
      isViewed: json['is_viewed'] as bool?,
      status: json['status'] as String?,
    );

Map<String, dynamic> _$NotificationDtoToJson(NotificationDto instance) => <String, dynamic>{
      'notification_id': instance.notificationId,
      'user_id': instance.userId,
      'title': instance.title,
      'description': instance.description,
      'date': instance.date,
      'is_viewed': instance.isViewed,
      'status': instance.status,
    };
