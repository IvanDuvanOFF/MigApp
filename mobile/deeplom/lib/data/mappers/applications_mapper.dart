import 'package:deeplom/data/dto/applications_dto.dart';
import 'package:deeplom/data/models/applications_model.dart';

extension ApplicationsDtoExt on ApplicationsDto {
  ApplicationsModel toModel() {
    return ApplicationsModel(
      id: id.toString(),
      title: title ?? '',
      status: ApplicationStatus.values.firstWhere(
        (it) => it.statusName == status,
        orElse: () => ApplicationStatus.in_progress, // Используйте значение по умолчанию
      ),
      date: date ?? '',
    );
  }
}

extension SelectedApplicationDtoExt on SelectedApplicationDto {
  SelectedApplicationModel toModel() {
    return SelectedApplicationModel(
      id: id.toString(),
      title: title ?? '',
      status: ApplicationStatus.values.firstWhere(
        (it) => it.statusName == status,
        orElse: () => ApplicationStatus.in_progress, // Используйте значение по умолчанию
      ),
      date: date ?? '',
      documents: documents?.map((doc) => doc.toModel()).toList() ?? [],
    );
  }
}

extension DocumentDtoExt on DocumentDto {
  DocumentModel toModel() {
    return DocumentModel(
      id: id.toString(),
      title: title ?? '',
      status: ApplicationStatus.values.firstWhere(
        (it) => it.statusName == status,
        orElse: () => ApplicationStatus.in_progress, // Используйте значение по умолчанию
      ),
      creationDate: '',
      expirationDate: '',
      fileName: '',
    );
  }
}
