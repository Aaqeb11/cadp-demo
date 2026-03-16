import { useState } from "react";
import { createUser } from "../api/api";

const INITIAL = { name: "", email: "", ssn: "" };

export default function UserForm() {
  const [form, setForm] = useState(INITIAL);
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(false);
  const [savedId, setSavedId] = useState(null);

  const handleChange = (e) =>
    setForm((prev) => ({ ...prev, [e.target.name]: e.target.value }));

  const handleSubmit = async (e) => {
    e.preventDefault();
    setLoading(true);
    setStatus(null);
    try {
      const res = await createUser(form);
      setSavedId(res.id);
      setStatus({
        type: "success",
        msg: `User saved successfully. ID: ${res.id}`,
      });
      setForm(INITIAL);
    } catch (err) {
      setStatus({ type: "error", msg: err.message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-6">
      <h2 className="text-lg font-semibold mb-1">Submit User</h2>
      <p className="text-gray-400 text-sm mb-5">
        Email and SSN are encrypted by CADP before being stored in the database.
      </p>

      <form onSubmit={handleSubmit} className="space-y-4">
        <div>
          <label className="block text-sm text-gray-300 mb-1">
            Name{" "}
            <span className="text-xs bg-gray-700 text-gray-400 px-1.5 py-0.5 rounded ml-1">
              plain
            </span>
          </label>
          <input
            name="name"
            placeholder="Aaqeb Ahmed"
            value={form.name}
            onChange={handleChange}
            required
            className="w-full bg-gray-950 border border-gray-700 rounded-md px-3 py-2 text-sm text-gray-100 focus:outline-none focus:border-blue-500"
          />
        </div>

        <div>
          <label className="block text-sm text-gray-300 mb-1">
            Email{" "}
            <span className="text-xs bg-blue-900 text-blue-300 px-1.5 py-0.5 rounded ml-1">
              encrypted
            </span>
          </label>
          <input
            name="email"
            type="email"
            placeholder="aaqeb@example.com"
            value={form.email}
            onChange={handleChange}
            required
            className="w-full bg-gray-950 border border-gray-700 rounded-md px-3 py-2 text-sm text-gray-100 focus:outline-none focus:border-blue-500"
          />
        </div>

        <div>
          <label className="block text-sm text-gray-300 mb-1">
            SSN{" "}
            <span className="text-xs bg-blue-900 text-blue-300 px-1.5 py-0.5 rounded ml-1">
              encrypted
            </span>
          </label>
          <input
            name="ssn"
            placeholder="123-45-6789"
            value={form.ssn}
            onChange={handleChange}
            required
            className="w-full bg-gray-950 border border-gray-700 rounded-md px-3 py-2 text-sm text-gray-100 focus:outline-none focus:border-blue-500"
          />
        </div>

        <button
          type="submit"
          disabled={loading}
          className="w-full bg-blue-600 hover:bg-blue-700 disabled:bg-gray-700 disabled:cursor-not-allowed text-white text-sm font-medium py-2 rounded-md transition-colors cursor-pointer"
        >
          {loading ? "Encrypting & Saving..." : "Submit"}
        </button>
      </form>

      {status && (
        <p
          className={`mt-4 text-sm px-3 py-2 rounded-md ${
            status.type === "success"
              ? "bg-green-900/40 text-green-400 border border-green-800"
              : "bg-red-900/40 text-red-400 border border-red-800"
          }`}
        >
          {status.msg}
        </p>
      )}

      {savedId && (
        <p className="mt-2 text-xs text-gray-500">
          Use ID{" "}
          <span className="text-blue-400 font-mono font-semibold">
            {savedId}
          </span>{" "}
          in the Lookup tab to verify decryption.
        </p>
      )}
    </div>
  );
}
