import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/file_dto.g.dart';

@JsonSerializable()
class FileDto extends Equatable {
  final String? status;
  final String? message;

  const FileDto({
    this.status,
    this.message,
  });

  factory FileDto.fromJson(Map<String, dynamic> json) => _$FileDtoFromJson(json);
  Map<String, dynamic> toJson() => _$FileDtoToJson(this);

  @override
  List<Object?> get props => [status, message];
}
