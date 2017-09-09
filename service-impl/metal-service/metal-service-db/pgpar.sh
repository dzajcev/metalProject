#! /bin/sh

PLATFORM=$(uname)
case "$PLATFORM" in
	"Darwin") ./pgpar-osx-amd64 "$URL" "$EDB_USR" "$EDB_PWD" ${1+"$@"} ;;
	"Linux") ./pgpar-linux-amd64 "$URL" "$EDB_USR" "$EDB_PWD" ${1+"$@"} ;;
	*) echo "unknown platform: $PLATFORM" ;;
esac
