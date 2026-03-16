import { useState } from "react";
import { uploadFile, downloadFile } from "../api/api";

export default function FileUpload() {
  const [file, setFile] = useState(null);
  const [status, setStatus] = useState(null);
  const [uploading, setUploading] = useState(false);
  const [lookupId, setLookupId] = useState("");

  const handleUpload = async () => {
    if (!file) return;
    setUploading(true);
    setStatus(null);
    try {
      const res = await uploadFile(file);
      setStatus({
        type: "success",
        msg: `File encrypted & stored. ID: ${res.id}`,
      });
      setLookupId(String(res.id));
    } catch (err) {
      setStatus({ type: "error", msg: err.message });
    } finally {
      setUploading(false);
    }
  };

  const handleDownload = async () => {
    try {
      const res = await downloadFile(lookupId);
      const blob = await res.blob();
      const disposition = res.headers.get("Content-Disposition");
      const filename = disposition
        ? disposition.split('filename="')[1].replace('"', "")
        : "downloaded_file";
      const url = window.URL.createObjectURL(blob);
      const a = document.createElement("a");
      a.href = url;
      a.download = filename;
      a.click();
      window.URL.revokeObjectURL(url);
    } catch (err) {
      setStatus({ type: "error", msg: err.message });
    }
  };

  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-6 space-y-6">
      <div>
        <h2 className="text-lg font-semibold mb-1">File Upload</h2>
        <p className="text-gray-400 text-sm">
          File is split into 2048-byte chunks, each encrypted individually by
          CADP, then stored as BYTEA in PostgreSQL.
        </p>
      </div>

      {/* Upload */}
      <div className="border border-gray-800 rounded-lg p-4 space-y-3">
        <h3 className="text-sm font-semibold text-gray-300">
          Encrypt & Upload
        </h3>

        <label className="flex flex-col items-center justify-center w-full h-28 border-2 border-dashed border-gray-700 rounded-lg cursor-pointer hover:border-blue-500 transition-colors">
          <span className="text-gray-400 text-sm">
            {file ? file.name : "Click to select a file"}
          </span>
          {file && (
            <span className="text-gray-500 text-xs mt-1">
              {(file.size / 1024).toFixed(1)} KB
            </span>
          )}
          <input
            type="file"
            className="hidden"
            onChange={(e) => setFile(e.target.files[0])}
          />
        </label>

        <button
          onClick={handleUpload}
          disabled={!file || uploading}
          className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-700 disabled:cursor-not-allowed text-white text-sm font-medium py-2 rounded-md transition-colors cursor-pointer"
        >
          {uploading ? "Encrypting & Uploading..." : "Upload & Encrypt"}
        </button>
      </div>

      {status && (
        <p
          className={`text-sm px-3 py-2 rounded-md ${
            status.type === "success"
              ? "bg-green-900/40 text-green-400 border border-green-800"
              : "bg-red-900/40 text-red-400 border border-red-800"
          }`}
        >
          {status.msg}
        </p>
      )}

      {/* Download */}
      <div className="border border-gray-800 rounded-lg p-4 space-y-3">
        <h3 className="text-sm font-semibold text-gray-300">
          Decrypt & Download
        </h3>
        <div className="flex gap-2">
          <input
            type="number"
            placeholder="Enter file ID"
            value={lookupId}
            onChange={(e) => setLookupId(e.target.value)}
            className="flex-1 bg-gray-950 border border-gray-700 rounded-md px-3 py-2 text-sm text-gray-100 focus:outline-none focus:border-blue-500"
          />
          <button
            onClick={handleDownload}
            disabled={!lookupId}
            className="bg-green-700 hover:bg-green-600 disabled:bg-gray-700 disabled:cursor-not-allowed text-white text-sm font-medium px-4 py-2 rounded-md transition-colors cursor-pointer"
          >
            Download
          </button>
        </div>
      </div>
    </div>
  );
}
