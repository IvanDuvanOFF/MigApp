import 'package:deeplom/config/proxy.dart';
import 'package:flutter/material.dart';
import 'package:shared_preferences/shared_preferences.dart';

TextEditingController _proxyController = TextEditingController();

showDevAlert(BuildContext context) {
  print('TEST');
  return showDialog(
      context: context,
      builder: (context) {
        return AlertDialog(
          content: Column(
            mainAxisSize: MainAxisSize.min,
            children: [
              TextField(
                controller: _proxyController,
                decoration: const InputDecoration(labelText: 'Proxy (e.g., 192.168.1.199:8888)'),
              ),
              const SizedBox(height: 16),
              ElevatedButton(
                onPressed: () => _saveProxy(context),
                child: const Text('Save Proxy'),
              ),
            ],
          ),
        );
      });
}

Future<void> _saveProxy(context) async {
  SharedPreferences prefs = await SharedPreferences.getInstance();
  String proxy = _proxyController.text;
  await prefs.setString('proxy', proxy);

  CustomProxy proxyConfig = CustomProxy.fromString(proxy: proxy)!;
  proxyConfig.enable();

  ScaffoldMessenger.of(context).showSnackBar(SnackBar(content: Text('Proxy set to $proxy')));
}
