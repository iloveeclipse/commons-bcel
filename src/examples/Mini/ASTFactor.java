/*
 * Copyright  2000-2004 The Apache Software Foundation
 *
 *  Licensed under the Apache License, Version 2.0 (the "License"); 
 *  you may not use this file except in compliance with the License.
 *  You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 *  Unless required by applicable law or agreed to in writing, software
 *  distributed under the License is distributed on an "AS IS" BASIS,
 *  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *  See the License for the specific language governing permissions and
 *  limitations under the License. 
 *
 */
/* Generated By:JJTree: Do not edit this line. ASTFactor.java */
/* JJT: 0.3pre1 */

package Mini;

/**
 *
 * @version $Id$
 * @author <A HREF="mailto:m.dahm@gmx.de">M. Dahm</A>
 */
public class ASTFactor extends ASTExpr {
  // Generated methods
  ASTFactor(int id) {
    super(id);
  }

  ASTFactor(MiniParser p, int id) {
    super(p, id);
  }

  public static Node jjtCreate(MiniParser p, int id) {
    return new ASTFactor(p, id);
  }

  // Inherited closeNode(), dump()

  /**
   * Drop this node, if kind == -1, because then it has just one child node
   * and may be safely replaced with it.
   */
  public ASTExpr traverse(Environment env) {
    if(kind == -1) {
        return exprs[0].traverse(env);
    } else {
        return new ASTExpr(exprs, kind, line, column).traverse(env);
    }
  }
}