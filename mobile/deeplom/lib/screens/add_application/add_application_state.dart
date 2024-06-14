import 'package:deeplom/config/lce.dart';
import 'package:deeplom/data/models/applications_model.dart';
import 'package:equatable/equatable.dart';

class AddApplicationState extends Equatable {
  final String? fileName;
  final bool isFileUploaded;
  final bool isDocumentAdded;
  final Lce<List<ApplicationsModel>> applications;
  final Lce<List<ApplicationsModel>> newApplications;
  final Lce<SelectedApplicationModel> selectedApplication;

  const AddApplicationState({
    this.fileName,
    this.isFileUploaded = false,
    this.isDocumentAdded = false,
    this.applications = const Lce.loading(),
    this.newApplications = const Lce.loading(),
    this.selectedApplication = const Lce.loading(),
  });

  AddApplicationState copyWith({
    String? fileName,
    bool? isFileUploaded,
    Lce<List<ApplicationsModel>>? applications,
    Lce<List<ApplicationsModel>>? newApplications,
    bool? isDocumentAdded,
    Lce<SelectedApplicationModel>? selectedApplication,
  }) {
    return AddApplicationState(
      fileName: fileName ?? this.fileName,
      isFileUploaded: isFileUploaded ?? this.isFileUploaded,
      isDocumentAdded: isDocumentAdded ?? this.isDocumentAdded,
      applications: applications ?? this.applications,
      newApplications: newApplications ?? this.newApplications,
      selectedApplication: selectedApplication ?? this.selectedApplication,
    );
  }

  @override
  List<Object?> get props => [fileName, isFileUploaded, isDocumentAdded, applications, selectedApplication, newApplications];
}
