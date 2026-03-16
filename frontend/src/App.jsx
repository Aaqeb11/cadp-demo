import { useState } from "react";
import UserForm from "./components/UserForm";
import UserLookup from "./components/UserLookup";
import FileUpload from "./components/FileUpload";

const TABS = ["Submit User", "Lookup User", "File Upload"];

export default function App() {
  const [activeTab, setActiveTab] = useState("Submit User");

  return (
    <div className="min-h-screen bg-gray-950 text-gray-100">
      <div className="max-w-2xl mx-auto px-4 py-10">
        {/* Header */}
        <header className="text-center mb-8">
          <h1 className="text-3xl font-bold mb-1">🔐 CADP Encryption Demo</h1>
          <p className="text-gray-400 text-sm">
            Powered by Thales CipherTrust Manager
          </p>
        </header>

        {/* Tabs */}
        <nav className="flex gap-2 mb-6 border-b border-gray-800 pb-3">
          {TABS.map((tab) => (
            <button
              key={tab}
              onClick={() => setActiveTab(tab)}
              className={`px-4 py-1.5 rounded-md text-sm font-medium transition-colors cursor-pointer
                ${
                  activeTab === tab
                    ? "bg-blue-600 text-white"
                    : "bg-transparent border border-gray-700 text-gray-400 hover:text-white hover:border-blue-500"
                }`}
            >
              {tab}
            </button>
          ))}
        </nav>

        {/* Tab Content */}
        <main>
          {activeTab === "Submit User" && <UserForm />}
          {activeTab === "Lookup User" && <UserLookup />}
          {activeTab === "File Upload" && <FileUpload />}
        </main>
      </div>
    </div>
  );
}
