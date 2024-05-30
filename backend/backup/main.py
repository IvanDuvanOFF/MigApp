import os
import shutil
import hashlib
import argparse
import zipfile

backup_filename = 'backup'
backup_extension = 'zip'
checksum_filename = 'checksum.sha256'


class RestoreException(Exception):
    pass


class CopyException(Exception):
    pass


class BackupException(Exception):
    pass


def get_checksum(directory):
    hasher = hashlib.sha256()
    for root, _, files in os.walk(directory):
        for filename in files:
            file_path = os.path.join(root, filename)
            with open(file_path, "rb") as file:
                file_hash = hashlib.sha256(file.read()).hexdigest()
                hasher.update(file_hash.encode())

    return hasher.hexdigest()


def restore(destination, backup):
    if not os.path.exists(destination):
        os.makedirs(destination)

    source_archive = os.path.join(backup, f'{backup_filename}.{backup_extension}')
    destination_archive = os.path.join(destination, f'{backup_filename}.{backup_extension}')

    shutil.copy2(source_archive, destination_archive)

    try:
        with zipfile.ZipFile(destination_archive, "r") as archive:
            archive.extractall(destination)
    except zipfile.BadZipFile as err:
        print(f"Error: Invalid archive format. {err}")
        os.remove(destination_archive)
        return

    os.remove(destination_archive)


def copy_data(source, destination):
    if not os.path.exists(source):
        raise FileNotFoundError('Source directory does not exist.')
    if not os.path.exists(destination):
        os.makedirs(destination)

    shutil.copytree(source, destination, dirs_exist_ok=True)  # Continue if destination dirs exist


parser = argparse.ArgumentParser(description="Copy files from source to destination.")
parser.add_argument("-a", "--action", required=True, help="What action should be performed.")
parser.add_argument("-s", "--source", required=True, help="Path to the source folder.")
parser.add_argument("-d", "--destination", required=True, help="Path to the destination folder.")
parser.add_argument("-b", "--backup", required=True, help="Path to the backup folder.")

args = parser.parse_args()
action = args.action
src = args.source
bkp = args.backup
dist = args.destination

backup_file = f'{bkp}/{backup_filename}.{backup_extension}'

if action == 'check':
    try:
        try:
            if os.listdir(dist):
                if os.path.exists(backup_file):
                    try:
                        with zipfile.ZipFile(backup_file) as z:
                            with z.open(checksum_filename, 'r') as checksum_file:
                                backup_checksum = checksum_file.read()
                    except (zipfile.BadZipFile, FileNotFoundError) as e:
                        backup_checksum = '-1'

                    try:
                        with open(f'{dist}/{checksum_filename}', 'rb') as checksum_file:
                            dist_checksum = checksum_file.read()
                    except FileNotFoundError:
                        dist_checksum = '-1'

                    if backup_checksum != '-1' and backup_checksum != dist_checksum:
                        raise RestoreException()
                else:
                    if not os.listdir(dist):
                        raise CopyException
            else:
                if os.path.exists(backup_file):
                    raise RestoreException
                else:
                    raise CopyException
        except RestoreException:
            print('Data is not actual... Restoring...')
            restore(dist, bkp)
        except CopyException:
            print('No data and no backup files found... Restoring...')
            copy_data(src, dist)
    except FileNotFoundError as e:
        print(f'{e}')
    except Exception as e:
        print('Unexpected error:', e)
elif action == 'backup':
    try:
        if not os.listdir(dist):
            raise BackupException

        content_hash = get_checksum(dist)

        with open(f'{dist}/{checksum_filename}', "w") as checksum_file:
            checksum_file.write(content_hash)

        archived = shutil.make_archive(f'{bkp}/{backup_filename}', backup_extension, dist)

        print('Backup completed')
    except BackupException:
        print('Backup failed. Nothing to backup')
    except Exception as e:
        print('Unexpected error:', e)
else:
    print('Invalid action')
