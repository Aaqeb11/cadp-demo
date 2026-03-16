import { useState } from "react";
import { getUser } from "../api/api";

export default function UserLookup() {
  const [id, setId] = useState("");
  const [user, setUser] = useState(null);
  const [status, setStatus] = useState(null);
  const [loading, setLoading] = useState(false);

  const handleLookup = async (e) => {
    e.preventDefault();
    setLoading(true);
    setStatus(null);
    setUser(null);
    try {
      const data = await getUser(id);
      setUser(data);
    } catch (err) {
      setStatus({ type: "error", msg: err.message });
    } finally {
      setLoading(false);
    }
  };

  return (
    <div className="bg-gray-900 border border-gray-800 rounded-xl p-6">
      <h2 className="text-lg font-semibold mb-1">Lookup User</h2>
      <p className="text-gray-400 text-sm mb-5">
        Fetches encrypted bytes from PostgreSQL and decrypts them via CADP on
        the backend.
      </p>

      <form onSubmit={handleLookup} className="flex gap-2">
        <input
          type="number"
          placeholder="Enter user ID"
          value={id}
          onChange={(e) => setId(e.target.value)}
          required
          className="flex-1 bg-gray-950 border border-gray-700 rounded-md px-3 py-2 text-sm text-gray-100 focus:outline-none focus:border-blue-500"
        />
        <button
          type="submit"
          disabled={loading}
          className="bg-blue-600 hover:bg-blue-700 disabled:bg-gray-700 disabled:cursor-not-allowed text-white text-sm font-medium px-4 py-2 rounded-md transition-colors cursor-pointer"
        >
          {loading ? "Decrypting..." : "Lookup"}
        </button>
      </form>

      {status && (
        <p className="mt-4 text-sm px-3 py-2 rounded-md bg-red-900/40 text-red-400 border border-red-800">
          {status.msg}
        </p>
      )}

      {user && (
        <div className="mt-5">
          <h3 className="text-sm font-semibold text-gray-300 mb-3">
            Decrypted Result
          </h3>
          <div className="bg-gray-950 border border-gray-800 rounded-lg overflow-hidden">
            {[
              { label: "ID", value: user.id, encrypted: false },
              { label: "Name", value: user.name, encrypted: false },
              { label: "Email", value: user.email, encrypted: true },
              { label: "SSN", value: user.ssn, encrypted: true },
            ].map((row) => (
              <div
                key={row.label}
                className="flex items-center justify-between px-4 py-2.5 border-b border-gray-800 last:border-0"
              >
                <span className="text-gray-400 text-sm w-16">{row.label}</span>
                <span className="text-gray-100 text-sm font-mono">
                  {row.value}
                </span>
                {row.encrypted ? (
                  <span className="text-xs bg-blue-900 text-blue-300 px-1.5 py-0.5 rounded">
                    decrypted
                  </span>
                ) : (
                  <span className="text-xs bg-gray-700 text-gray-400 px-1.5 py-0.5 rounded">
                    plain
                  </span>
                )}
              </div>
            ))}
          </div>
        </div>
      )}
    </div>
  );
}
