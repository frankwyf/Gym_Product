module.exports = {
  testEnvironment: "node",
  moduleFileExtensions: ["js", "json"],
  moduleNameMapper: {
    "^@/(.*)$": "<rootDir>/src/$1"
  },
  transform: {
    "^.+\\.js$": "babel-jest"
  },
  testMatch: ["<rootDir>/tests/unit/**/*.spec.js"]
};
