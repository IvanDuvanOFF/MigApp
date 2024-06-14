import 'package:deeplom/config/navigation.dart';
import 'package:deeplom/domain/repositories/main/abstract_main_repository.dart';
import 'package:deeplom/generated/l10n.dart';
import 'package:flutter/material.dart';
import 'package:get_it/get_it.dart';
import 'package:url_launcher/url_launcher.dart';

class FaqScreen extends StatefulWidget {
  const FaqScreen({super.key});

  @override
  State<FaqScreen> createState() => _FaqScreenState();
}

class _FaqScreenState extends State<FaqScreen> {
  final mainRepository = GetIt.I<AbstractMainRepository>();

  @override
  void initState() {
    super.initState();
  }

  void getRemember() async {
    await mainRepository.getRemember();
  }

  @override
  Widget build(BuildContext context) {
    return Scaffold(
      backgroundColor: Colors.white,
      body: SafeArea(
        child: Center(
          child: Padding(
            padding: const EdgeInsets.all(16.0),
            child: Column(
              crossAxisAlignment: CrossAxisAlignment.start,
              children: [
                Padding(
                  padding: const EdgeInsets.all(24.0),
                  child: Image.asset(
                    'assets/logo.jpeg',
                    scale: 3,
                  ),
                ),
                Container(
                  height: MediaQuery.of(context).size.height / 1.5,
                  decoration: BoxDecoration(
                    color: Colors.white,
                    borderRadius: BorderRadius.circular(8.0),
                    boxShadow: const [
                      BoxShadow(
                        color: Colors.black12,
                        blurRadius: 10.0,
                        spreadRadius: 1.0,
                      ),
                    ],
                  ),
                  child: Padding(
                    padding: const EdgeInsets.all(16.0),
                    child: Column(
                      crossAxisAlignment: CrossAxisAlignment.start,
                      children: [
                        InkWell(
                          onTap: () {
                            getRemember();
                            showFinishedFileDialog(context);
                          },
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                S.of(context).studentFileText,
                                style: const TextStyle(fontSize: 16.0),
                              ),
                              IconButton(
                                icon: const Icon(Icons.download),
                                onPressed: () {
                                  // Add your download action here
                                },
                              ),
                            ],
                          ),
                        ),
                        const SizedBox(height: 16.0),
                        InkWell(
                          onTap: _openTelegramChat,
                          child: Row(
                            mainAxisAlignment: MainAxisAlignment.spaceBetween,
                            children: [
                              Text(
                                S.of(context).goToEmployeeChat,
                                style: const TextStyle(fontSize: 16.0),
                              ),
                              IconButton(
                                icon: const Icon(Icons.send),
                                onPressed: () {
                                  // Add your navigation action here
                                },
                              ),
                            ],
                          ),
                        ),
                      ],
                    ),
                  ),
                ),
              ],
            ),
          ),
        ),
      ),
    );
  }
}

void _openTelegramChat() async {
  const telegramUrl = 'https://t.me/ZEDTimeToWakeUp';

  await launchUrl(Uri.parse(telegramUrl));
}

showFinishedFileDialog(BuildContext context) {
  showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          content: SizedBox(
            height: 96,
            width: 100,
            child: Center(
              child: Column(
                mainAxisSize: MainAxisSize.min,
                children: [
                  Text(S.of(context).fileUpload),
                  const SizedBox(height: 12.0),
                  TextButton(
                    onPressed: () => AppRouting.pop(),
                    child: Text(
                      S.of(context).okText,
                      style: const TextStyle(color: Colors.black, fontSize: 16.0),
                    ),
                  ),
                ],
              ),
            ),
          ),
        );
      });
}
