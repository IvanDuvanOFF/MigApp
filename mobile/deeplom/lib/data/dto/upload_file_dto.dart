import 'package:equatable/equatable.dart';
import 'package:json_annotation/json_annotation.dart';

part 'generated/upload_file_dto.g.dart';

@JsonSerializable()
class UploadFileDto extends Equatable {
  final String? name;
  final String? link;

  const UploadFileDto({
    this.name,
    this.link,
  });

  factory UploadFileDto.fromJson(Map<String, dynamic> json) => _$UploadFileDtoFromJson(json);
  Map<String, dynamic> toJson() => _$UploadFileDtoToJson(this);

  @override
  List<Object?> get props => [name, link];
}
