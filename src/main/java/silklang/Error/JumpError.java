/*
 * Copyright (c) under GPL V3. Read LICENSE located in the root of the project.
 * All rights reserved.
 */

package silklang.Error;

public class JumpError extends RuntimeException {

	 private JumpType type;

	 public JumpError(JumpType type) {
		  this.type = type;
	 }

	 public JumpType getType() {
		  return type;
	 }
}
