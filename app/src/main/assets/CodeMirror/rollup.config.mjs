import {nodeResolve} from "@rollup/plugin-node-resolve"
export default {
  input: "./index.mjs",
  output: {
    file: "./index.bundle.js",
    format: "iife"
  },
  plugins: [nodeResolve()]
}