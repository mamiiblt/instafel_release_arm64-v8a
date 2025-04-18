package me.mamiiblt.instafel.patcher.source;

public class SmaliParser {

    public static SmaliInstruction parseInstruction(String line, int num) {
        String[] parts = line.trim().split("\\s+", 2);
        if (parts.length < 2) {
            throw new IllegalArgumentException("Invalid smali format " + line.trim());
        }

        String opcode = parts[0];
        String remainder = parts[1];

        int braceCloseIndex = remainder.indexOf('}');
        if (braceCloseIndex == -1) {
            throw new IllegalArgumentException("Invalid register format " + line.trim());
        }

        String registersPart = remainder.substring(0, braceCloseIndex + 1);
        String methodPart = remainder.substring(braceCloseIndex + 2);

        String registersContent = registersPart.substring(registersPart.indexOf('{') + 1, registersPart.indexOf('}'));
        String[] registers = registersContent.split("\\s*,\\s*");

        int arrowIndex = methodPart.indexOf("->");
        if (arrowIndex == -1) {
            throw new IllegalArgumentException("Invalid method format " + line.trim());
        }

        String className = methodPart.substring(0, arrowIndex);
        String methodAndReturn = methodPart.substring(arrowIndex + 2);

        int paramsStart = methodAndReturn.indexOf('(');
        int paramsEnd = methodAndReturn.indexOf(')');
        if (paramsStart == -1 || paramsEnd == -1 || paramsEnd < paramsStart) {
            throw new IllegalArgumentException("Invalid method parameter format " + line.trim());
        }

        String methodName = methodAndReturn.substring(0, paramsStart);
        String returnType = methodAndReturn.substring(paramsEnd + 1);

        return new SmaliInstruction(num, opcode.trim(), registers, className.trim(), methodName.translateEscapes(), returnType.trim());
    }

    public static class SmaliInstruction {
        private String opcode;
        private String[] registers;
        private String className;
        private String methodName;
        private String returnType;
        private int num;

        public SmaliInstruction(int num, String opcode, String[] registers, String className, String methodName, String returnType) {
            this.num = num;
            this.opcode = opcode;
            this.registers = registers;
            this.className = className;
            this.methodName = methodName;
            this.returnType = returnType;
        }
    
    
        @Override
        public String toString() {
            return "Opcode: " + opcode + "\n" +
                   "Registers: " + String.join(", ", registers) + "\n" +
                   "Class Name: " + className + "\n" +
                   "Method Name: " + methodName + "\n" +
                   "Return Type: " + returnType;
        }

        public int getNum() {
            return num;
        }
    
        public String getOpcode() {
            return opcode;
        }
    
        public String[] getRegisters() {
            return registers;
        }
    
        public String getClassName() {
            return className;
        }
    
        public String getMethodName() {
            return methodName;
        }
    
        public String getReturnType() {
            return returnType;
        }
    }
}
