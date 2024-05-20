import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/error_dto.g.dart';

@JsonSerializable()
class ErrorDto extends Equatable {
  final StatusDto? status;
  final String? message;

  const ErrorDto({
    this.status,
    this.message,
  });

  factory ErrorDto.fromJson(Map<String, dynamic> json) => _$ErrorDtoFromJson(json);
  Map<String, dynamic> toJson() => _$ErrorDtoToJson(this);

  @override
  List<Object?> get props => [status, message];
}

@JsonSerializable()
class StatusDto extends Equatable {
  final int? code;
  final String? name;

  const StatusDto({
    this.code,
    this.name,
  });

  factory StatusDto.fromJson(Map<String, dynamic> json) => _$StatusDtoFromJson(json);
  Map<String, dynamic> toJson() => _$StatusDtoToJson(this);

  @override
  List<Object?> get props => [code, name];
}
