import 'package:deeplom/data/dto/applications_dto.dart';
import 'package:deeplom/data/models/applications_model.dart';

extension ApplicationsDtoExt on ApplicationsDto {
  ApplicationsModel toModel() {
    return ApplicationsModel(
      id: id.toString(),
      title: title ?? '',
      status: ApplicationStatus.values.firstWhere((it) => it.name == status),
    );
  }
}

extension SelectedApplicationDtoExt on SelectedApplicationDto {
  SelectedApplicationModel toModel() {
    return SelectedApplicationModel(
      id: id.toString(),
      title: title ?? '',
      status: status ?? '',
      documents: documents?.map((doc) => doc.toModel()).toList() ?? [],
    );
  }
}

extension DocumentDtoExt on DocumentDto {
  DocumentModel toModel() {
    return DocumentModel(
      id: id.toString(),
      title: title ?? '',
      link: link ?? '',
      status: status ?? '',
    );
  }
}
