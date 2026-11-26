#!/usr/bin/env python3
"""Local HTTP server for testing Compose Multiplatform Wasm builds with COOP/COEP headers."""
import argparse
import http.server
import os
import socketserver
import sys

DEV_DIR = "composeApp/build/dist/wasmJs/developmentExecutable"
PROD_DIR = "composeApp/build/dist/wasmJs/productionExecutable"


class CrossOriginIsolatedHandler(http.server.SimpleHTTPRequestHandler):
    extensions_map = {
        **http.server.SimpleHTTPRequestHandler.extensions_map,
        ".wasm": "application/wasm",
        ".js": "application/javascript",
        ".mjs": "application/javascript",
    }

    def end_headers(self):
        # Cross-Origin Isolation headers required for Wasm (SharedArrayBuffer / threads)
        # "same-origin-allow-popups" keeps OAuth/Sign-In popups working.
        self.send_header("Cross-Origin-Opener-Policy", "same-origin-allow-popups")
        self.send_header("Cross-Origin-Embedder-Policy", "require-corp")
        super().end_headers()


def main():
    parser = argparse.ArgumentParser(description="Serve Compose Wasm distribution locally.")
    parser.add_argument("--port", type=int, default=8080, help="Port to bind (default: 8080)")
    parser.add_argument(
        "--prod",
        action="store_true",
        help=f"Serve production bundle ({PROD_DIR}) instead of dev ({DEV_DIR})",
    )
    args = parser.parse_args()

    directory = PROD_DIR if args.prod else DEV_DIR
    if not os.path.exists(directory):
        task = (
            ":composeApp:wasmJsBrowserDistribution"
            if args.prod
            else ":composeApp:wasmJsBrowserDevelopmentDistribution"
        )
        print(f"ERROR: Directory not found: {directory}", file=sys.stderr)
        print(f"Run first: ./gradlew {task}", file=sys.stderr)
        sys.exit(1)

    handler = lambda *h_args, **h_kwargs: CrossOriginIsolatedHandler(
        *h_args, directory=directory, **h_kwargs
    )

    print(f"Serving Wasm app from: {directory}")
    print(f"Open: http://localhost:{args.port}")

    socketserver.TCPServer.allow_reuse_address = True
    with socketserver.TCPServer(("", args.port), handler) as httpd:
        try:
            httpd.serve_forever()
        except KeyboardInterrupt:
            pass


if __name__ == "__main__":
    main()

