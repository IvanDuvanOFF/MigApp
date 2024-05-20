class ApplicationsModel {
  final String id;
  final String title;
  final ApplicationStatus status;

  const ApplicationsModel({
    required this.id,
    required this.title,
    required this.status,
  });
}

enum ApplicationStatus {
  done('done'),
  loading('loading'),
  failed('failed'),
  in_progress('in_progress');

  final String statusName;

  const ApplicationStatus(this.statusName);
}

class SelectedApplicationModel {
  final String id;
  final String title;
  final String status;
  final List<DocumentModel> documents;

  const SelectedApplicationModel({
    required this.id,
    required this.title,
    required this.status,
    required this.documents,
  });
}

class DocumentModel {
  final String id;
  final String title;
  final String link;
  final String status;

  const DocumentModel({
    required this.id,
    required this.title,
    required this.link,
    required this.status,
  });
}
