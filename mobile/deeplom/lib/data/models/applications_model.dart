class ApplicationsModel {
  final String id;
  final String title;
  final ApplicationStatus status;
  final String date; // добавлено новое поле

  const ApplicationsModel({
    required this.id,
    required this.title,
    required this.status,
    required this.date, // добавлено новое поле
  });
}

enum ApplicationStatus {
  done('done'),
  loading('loading'),
  failed('failed'),
  in_progress('in_progress'),
  saved('SAVED');

  final String statusName;

  const ApplicationStatus(this.statusName);
}

class SelectedApplicationModel {
  final String id;
  final String title;
  final ApplicationStatus status;
  final String date;
  final List<DocumentModel> documents;

  const SelectedApplicationModel({
    required this.id,
    required this.title,
    required this.status,
    required this.date,
    required this.documents,
  });
}

class DocumentModel {
  final String id;
  final String title;
  final ApplicationStatus status;
  final String creationDate;
  final String expirationDate;
  final String fileName;
  const DocumentModel({
    required this.id,
    required this.title,
    required this.status,
    required this.creationDate,
    required this.expirationDate,
    required this.fileName,
  });
}
